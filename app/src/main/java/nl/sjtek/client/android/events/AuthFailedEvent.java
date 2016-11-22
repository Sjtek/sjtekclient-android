package nl.sjtek.client.android.events;

import com.android.volley.VolleyError;

/**
 * Event when authentication has failed.
 */
public class AuthFailedEvent {
    private final VolleyError volleyError;

    public AuthFailedEvent(VolleyError volleyError) {
        this.volleyError = volleyError;
    }

    public VolleyError getVolleyError() {
        return volleyError;
    }
}
