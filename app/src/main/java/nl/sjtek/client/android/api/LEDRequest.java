package nl.sjtek.client.android.api;

import android.annotation.SuppressLint;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by Wouter Habets on 20-3-16.
 */
class LEDRequest extends Request<Boolean> {

    private static final String URL = "http://10.10.0.4:8000/led?rgb=%d,%d,%d";

    private final Response.Listener<Boolean> responseListener;


    @SuppressLint("DefaultLocale")
    LEDRequest(int r, int g, int b, Response.Listener<Boolean> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL, r, g, b), errorListener);
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
