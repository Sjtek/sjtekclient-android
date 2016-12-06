package nl.sjtek.client.android.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import nl.sjtek.control.data.actions.Action;
import nl.sjtek.control.data.settings.DataCollection;

/**
 * Request for getting data form the API.
 */
class DataRequest extends AuthenticatedRequest<DataCollection> {

    DataRequest(String credentials, Response.Listener<DataCollection> listener, Response.ErrorListener errorListener) {
        super(Method.GET, Action.DATA.toString(), credentials, listener, errorListener);
    }

    @Override
    protected Response<DataCollection> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            DataCollection dataCollection = DataCollection.fromJson(data);
            return Response.success(dataCollection, null);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
