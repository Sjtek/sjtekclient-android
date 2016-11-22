package nl.sjtek.client.android.api;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * A Volley request that will add basic authentication headers.
 *
 * @param <T>
 */
abstract class AuthenticatedRequest<T> extends Request<T> {

    private final String credentials;
    private final Response.Listener<T> listener;

    AuthenticatedRequest(int method, String url, String credentials, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.credentials = credentials;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (!TextUtils.isEmpty(credentials)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("Authorization", credentials);
            return params;
        } else {
            return super.getHeaders();
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
