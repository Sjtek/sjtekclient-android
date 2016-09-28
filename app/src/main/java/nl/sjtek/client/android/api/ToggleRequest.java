package nl.sjtek.client.android.api;

import android.text.TextUtils;

import com.android.volley.Response;

import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 25-3-16.
 */
public class ToggleRequest extends InfoRequest {

    private static final String BASE_URL = Action.SWITCH.getUrl() + "?voice&music&user=%s";

    public ToggleRequest(Response.Listener<ResponseCollection> responseListener,
                         Response.ErrorListener errorListener,
                         String credentials,
                         String user) {
        super(buildUrl(user), responseListener, errorListener, credentials);
    }

    private static String buildUrl(String user) {
        if (!TextUtils.isEmpty(user)) {
            user = "default";
        }
        return String.format(BASE_URL, user);
    }
}
