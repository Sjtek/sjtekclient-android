package nl.sjtek.client.android.api;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.sjtek.control.data.actions.Actions;
import nl.sjtek.control.data.parsers.UserHolder;
import nl.sjtek.control.data.parsers.UserParser;
import nl.sjtek.control.data.staticdata.User;

/**
 * Request for authenticating with the API.<br>
 * Will put the username and password as a Basic Authentication token in the header.
 * And will parse the result as a {@link UserHolder}.
 */
class AuthenticationRequest extends Request<List<User>> {

    private static final String URL = API.BASE_URL + Actions.INSTANCE.users();

    private final Response.Listener<List<User>> responseListener;
    private final String username;
    private final String password;

    AuthenticationRequest(String username, String password,
                          Response.Listener<List<User>> responseListener,
                          Response.ErrorListener listener) {
        super(Method.GET, URL, listener);
        this.responseListener = responseListener;
        this.username = username;
        this.password = password;
        setShouldCache(false);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<>();
        String credentials = String.format("%s:%s", username, password);
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
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
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(List<User> response) {
        responseListener.onResponse(response);
    }
}
