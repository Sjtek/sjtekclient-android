package nl.sjtek.client.android.api;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by wouter on 4-9-17.
 */

public class ActionRequest extends AuthenticatedRequest<Boolean> {
    private static final int INITIAL_TIMEOUT_MS = 10000;

    ActionRequest(String url, Response.Listener<Boolean> responseListener,
                  Response.ErrorListener errorListener,
                  String credentials) {
        super(Request.Method.GET, API.BASE_URL + url, credentials, responseListener, errorListener);
        Log.d(this.getClass().getSimpleName(), "Action: " + url);
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<Boolean> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            return Response.success(true, null);
        } else {
            return Response.error(new VolleyError());
        }
    }
}
