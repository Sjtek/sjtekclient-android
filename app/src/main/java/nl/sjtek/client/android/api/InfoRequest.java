package nl.sjtek.client.android.api;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Basic API request for sending actions. Expects a {@link ResponseCollection} response.
 */
class InfoRequest extends AuthenticatedRequest<ResponseCollection> {

    private static final int INITIAL_TIMEOUT_MS = 10000;

    InfoRequest(String url, Response.Listener<ResponseCollection> responseListener,
                Response.ErrorListener errorListener,
                String credentials) {
        super(Method.GET, url, credentials, responseListener, errorListener);
        Log.d(this.getClass().getSimpleName(), "URL: " + url);
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
}
