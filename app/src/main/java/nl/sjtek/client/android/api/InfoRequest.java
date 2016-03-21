package nl.sjtek.client.android.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import nl.sjtek.client.android.utils.Storage;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 29-1-16.
 */
public class InfoRequest extends Request<ResponseCollection> {

    private Response.Listener<ResponseCollection> responseListener;

    public InfoRequest(Response.Listener<ResponseCollection> responseListener,
                       Response.ErrorListener errorListener) {
        this(Action.REFRESH, responseListener, errorListener);
    }

    public InfoRequest(ActionInterface action,
                       Response.Listener<ResponseCollection> responseListener,
                       Response.ErrorListener errorListener) {
        this(action.getUrl(), responseListener, errorListener);
    }

    protected InfoRequest(String url, Response.Listener<ResponseCollection> responseListener,
                          Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        Log.d(this.getClass().getSimpleName(), "URL: " + url);
        this.responseListener = responseListener;
        setShouldCache(false);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String credentials = Storage.getInstance().getCredentials();
        if (Storage.getInstance().isCredentialsSet()) {
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
