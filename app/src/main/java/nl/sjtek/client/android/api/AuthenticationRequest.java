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
import java.util.Map;

import nl.sjtek.control.data.settings.DataCollection;

/**
 * Request for authenticating with the API.<br>
 * Will put the username and password as a Basic Authentication token in the header.
 * And will parse the result as a {@link DataCollection}.
 */
class AuthenticationRequest extends Request<DataCollection> {

    private static final String URL = Action.DATA.toString();

    private final Response.Listener<DataCollection> responseListener;
    private final String username;
    private final String password;

    AuthenticationRequest(String username, String password,
                          Response.Listener<DataCollection> responseListener,
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
    protected Response<DataCollection> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            DataCollection dataCollection = DataCollection.fromJson(data);
            return Response.success(dataCollection, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(DataCollection response) {
        responseListener.onResponse(response);
    }
}
