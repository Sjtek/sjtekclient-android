package nl.sjtek.client.android.events;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Event for network errors. Will automatically generated UI messages.
 */
public class NetworkErrorEvent {

    private final String message;
    private final boolean showSignIn;

    public NetworkErrorEvent(VolleyError error) {
        // TODO: 22-11-16 Remove hardcoded messages
        if (error instanceof NoConnectionError) {
            message = "Oops, no connection.";
            showSignIn = false;
        } else if (error instanceof TimeoutError) {
            message = "Oops, request timed out.";
            showSignIn = false;
        } else if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
            message = "Oops, you need to be signed in to use this app outside the Sjtek.";
            showSignIn = true;
        } else if (error.networkResponse != null) {
            message = "Error: " + error.networkResponse.statusCode + " - " + error.getMessage();
            showSignIn = false;
        } else {
            message = "Error: " + error.getMessage();
            showSignIn = false;
        }
    }

    public String getMessage() {
        return message;
    }

    public boolean isShowSignIn() {
        return showSignIn;
    }
}
