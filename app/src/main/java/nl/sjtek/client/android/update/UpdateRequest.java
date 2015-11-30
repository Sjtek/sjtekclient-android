package nl.sjtek.client.android.update;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Wouter Habets on 7-10-15.
 */
public class UpdateRequest extends Request<Update> {

    private static final String LOG_TAG = UpdateRequest.class.getSimpleName();
    private Response.Listener<Update> listener;

    public UpdateRequest(Response.Listener<Update> responseListener,
                         Response.ErrorListener errorListener) {
        super(Method.GET, Action.REFRESH.toString(), errorListener);
        this.listener = responseListener;
        Log.d(LOG_TAG, "Url: " + Action.REFRESH.toString());
    }

    public UpdateRequest(Action action, Response.Listener<Update> responseListener,
                         Response.ErrorListener errorListener) {
        super(Method.GET, action.toString(), errorListener);
        this.listener = responseListener;
        Log.d(LOG_TAG, "Url: " + action);
    }

    public UpdateRequest(Action.Music action, Response.Listener<Update> responseListener,
                         Response.ErrorListener errorListener) {
        super(Method.GET, action.toString(), errorListener);
        this.listener = responseListener;
        Log.d(LOG_TAG, "Url: " + action);
    }

    public UpdateRequest(Action.Light action, Response.Listener<Update> responseListener,
                         Response.ErrorListener errorListener) {
        super(Method.GET, action.toString(), errorListener);
        this.listener = responseListener;
        Log.d(LOG_TAG, "Url: " + action);
    }

    public UpdateRequest(Action.TV action, Response.Listener<Update> responseListener,
                         Response.ErrorListener errorListener) {
        super(Method.GET, action.toString(), errorListener);
        this.listener = responseListener;
        Log.d(LOG_TAG, "Url: " + action);
    }

    public UpdateRequest(String url, Response.Listener<Update> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = responseListener;
        Log.d(LOG_TAG, "Url: " + url);
    }

    @Override
    protected Response<Update> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Update update = new Update(new JSONObject(json));
            return Response.success(update, null);
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Update response) {
        listener.onResponse(response);
    }
}
