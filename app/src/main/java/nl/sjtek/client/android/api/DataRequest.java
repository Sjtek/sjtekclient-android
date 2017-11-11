package nl.sjtek.client.android.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.List;

import nl.sjtek.control.data.actions.Actions;
import nl.sjtek.control.data.parsers.UserHolder;
import nl.sjtek.control.data.parsers.UserParser;
import nl.sjtek.control.data.staticdata.User;

/**
 * Request for getting data form the API.
 */
@Deprecated
class DataRequest extends AuthenticatedRequest<List<User>> {

    DataRequest(String credentials, Response.Listener<List<User>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, Actions.INSTANCE.users(), credentials, listener, errorListener);
    }

    @Override
    protected Response<List<User>> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            UserHolder userHolder = UserParser.INSTANCE.parse(data);
            if (userHolder.getException() != null) {
                return Response.error(new ParseError(userHolder.getException()));
            }
            return Response.success(userHolder.getUsers(), null);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
