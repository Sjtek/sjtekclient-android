package nl.sjtek.client.android.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import nl.sjtek.control.data.actions.Actions;
import nl.sjtek.control.data.parsers.ResponseHolder;
import nl.sjtek.control.data.parsers.ResponseParser;

/**
 * Basic API request for sending actions. Expects a {@link ResponseHolder} response.
 */
class InfoRequest extends AuthenticatedRequest<ResponseHolder> {

    private static final int INITIAL_TIMEOUT_MS = 10000;

    InfoRequest(Response.Listener<ResponseHolder> responseListener,
                Response.ErrorListener errorListener,
                String credentials) {
        super(Method.GET, API.BASE_URL + Actions.INSTANCE.info(), credentials, responseListener, errorListener);
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<ResponseHolder> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            ResponseHolder responseHolder = ResponseParser.INSTANCE.parse(data);
            if (responseHolder.getException() != null) {
                return Response.error(new ParseError(responseHolder.getException()));
            }
            return Response.success(responseHolder, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
