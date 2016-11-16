package nl.sjtek.client.android.api;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Wouter Habets on 20-3-16.
 */
public class LEDRequest extends Request<Boolean> {

    private final Response.Listener<Boolean> responseListener;


    public LEDRequest(int code, Response.Listener<Boolean> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, "http://10.10.0.4:8000/led?code=" + code, errorListener);
        Log.d(this.getClass().getSimpleName(), "URL: " + getUrl());
        this.responseListener = responseListener;
    }

    @Override
    protected Response<Boolean> parseNetworkResponse(NetworkResponse response) {
        return Response.success(true, null);
    }

    @Override
    protected void deliverResponse(Boolean response) {
        this.responseListener.onResponse(response);
    }
}
