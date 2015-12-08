package nl.sjtek.client.android.api;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wouter Habets on 4-12-15.
 */
public class AuthenticationRequest extends Request<Boolean> {

    private static final String URL = Action.API_BASE + "";

    private final Response.Listener<Boolean> responseListener;
    private final String username;
    private final String password;

    public AuthenticationRequest(String username, String password,
                                 Response.Listener<Boolean> responseListener,
                                 Response.ErrorListener listener) {
        super(Method.GET, URL, listener);
        this.responseListener = responseListener;
        this.username = username;
        this.password = password;
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
    protected Response<Boolean> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) return Response.success(true, null);
        return Response.error(new VolleyError("Wrong status code"));
    }

    @Override
    protected void deliverResponse(Boolean response) {
        responseListener.onResponse(response);
    }
}
