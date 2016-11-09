package nl.sjtek.client.android.events;

import com.android.volley.VolleyError;

/**
 * Created by wouter on 9-11-16.
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
