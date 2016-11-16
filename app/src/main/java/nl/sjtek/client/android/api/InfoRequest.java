package nl.sjtek.client.android.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 29-1-16.
 */
class InfoRequest extends Request<ResponseCollection> {

    private static final int INITIAL_TIMEOUT_MS = 10000;
    private final String credentials;
    private final Response.Listener<ResponseCollection> responseListener;

    InfoRequest(String url, Response.Listener<ResponseCollection> responseListener,
                Response.ErrorListener errorListener,
                String credentials) {
        super(Method.GET, url, errorListener);
        Log.d(this.getClass().getSimpleName(), "URL: " + url);
        this.responseListener = responseListener;
        setShouldCache(false);
        this.credentials = credentials;
        setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (credentials != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("Authorization", credentials);
            return params;
        } else {
            return super.getHeaders();
        }
    }

    @Override
    protected Response<ResponseCollection> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            ResponseCollection responseCollection = new ResponseCollection(data);
            return Response.success(responseCollection, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ResponseCollection response) {
        this.responseListener.onResponse(response);
    }
}
