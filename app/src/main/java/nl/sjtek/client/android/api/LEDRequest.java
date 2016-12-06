package nl.sjtek.client.android.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Locale;

import nl.sjtek.control.data.actions.Action;

/**
 * Request for changing the LED strip.
 */
class LEDRequest extends Request<Boolean> {

    private static final String PATH = "%s?rgb=%d,%d,%d";

    private final Response.Listener<Boolean> responseListener;

    LEDRequest(int r, int g, int b, Response.Listener<Boolean> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(r, g, b), errorListener);
        this.responseListener = responseListener;
    }

    private static String buildUrl(int r, int g, int b) {
        if (r == 0 && g == 0 && b == 0) {
            return Action.Light.TOGGLE_3_OFF.getUrl();
        } else {
            return String.format(Locale.getDefault(), PATH, Action.Light.TOGGLE_3_ON, r, g, b);
        }
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
