package nl.sjtek.client.android.api;

import com.android.volley.Response;

import nl.sjtek.client.android.utils.Storage;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 25-3-16.
 */
public class ToggleRequest extends InfoRequest {

    private static final String BASE_URL = Action.SWITCH.getUrl() + "?voice&music&user=%s";

    public ToggleRequest(Response.Listener<ResponseCollection> responseListener, Response.ErrorListener errorListener) {
        super(buildUrl(), responseListener, errorListener);
    }

    private static String buildUrl() {
        String username;
        Storage storage = Storage.getInstance();
        if (storage.isCredentialsSet()) {
            username = storage.getUsername();
        } else {
            username = "default";
        }
        return String.format(BASE_URL, username);
    }
}
