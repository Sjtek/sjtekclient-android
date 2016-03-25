package nl.sjtek.client.android.api;

import com.android.volley.Response;

import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 20-3-16.
 */
public class LEDRequest extends InfoRequest {

    public LEDRequest(int code, boolean enable,
                      Response.Listener<ResponseCollection> responseListener,
                      Response.ErrorListener errorListener) {
        super(buildUrl(code, enable), responseListener, errorListener);
    }

    private static String buildUrl(int code, boolean enable) {
        return (enable ? Action.Light.TOGGLE_3_ON : Action.Light.TOGGLE_3_OFF) + "?code=" + code;
    }
}
