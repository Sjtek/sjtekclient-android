package nl.sjtek.client.android.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;

import nl.sjtek.client.android.events.AuthFailedEvent;
import nl.sjtek.client.android.events.AuthSuccessfulEvent;
import nl.sjtek.client.android.events.NetworkErrorEvent;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.control.data.actions.Actions;
import nl.sjtek.control.data.parsers.ResponseHolder;

/**
 * Helper class for communicating with the API.
 */
public class API implements Response.Listener<ResponseHolder>, Response.ErrorListener {

    public static final String BASE_URL = "https://sjtek.nl/api/";
    private static API instance;
    private final RequestQueue requestQueue;

    private API(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    private static synchronized API getInstance(Context context) {
        if (instance == null) {
            instance = new API(context);
        }

        return instance;
    }

    /**
     * Fills the static {@link API} instance.
     *
     * @param context Context
     */
    public static void init(Context context) {
        getInstance(context);
    }

    /**
     * Make an authentication attempt at the API by downloading the preferences.<br>
     * An {@link AuthSuccessfulEvent} or {@link AuthFailedEvent} will be sent over the bus on completion.
     *
     * @param context  Context
     * @param username Username
     * @param password Password
     */
    public static void authenticate(Context context, String username, String password) {
        getInstance(context).requestQueue.add(new AuthenticationRequest(username, password, response -> EventBus.getDefault().post(new AuthSuccessfulEvent(response)), error -> EventBus.getDefault().post(new AuthFailedEvent(error))));
    }

    public static void data(Context context) {
        RequestQueue queue = getInstance(context).requestQueue;
        String token = getToken(context);
        queue.add(new LampsRequest(token));
        queue.add(new QuotesRequest(token));
        queue.add(new UsersRequest(token));
    }

    /**
     * Change the color of the LED strip.<br>
     * This request will not send any events.
     *
     * @param context Context
     * @param r       Value for red
     * @param g       Value for green
     * @param b       Value for blue
     */
    @Deprecated
    public static void led(Context context, int r, int g, int b) {
        getInstance(context).addRequest(context, Actions.INSTANCE.getLights().toggle(8, "", true, r, g, b));
    }

    /**
     * Send an action to the API.<br>
     * This will send {@link ResponseHolder} or {@link NetworkErrorEvent} on completion.<br>
     * This request is authenticated.
     *
     * @param context Context
     * @param action  Action to send
     */
    public static void action(Context context, String action) {
        getInstance(context).addRequest(context, action);
    }

    /**
     * Get the state from the API.<br>
     * This will send {@link ResponseHolder} or {@link NetworkErrorEvent} on completion.<br>
     * This request is authenticated.
     *
     * @param context Context
     */
    public static void info(Context context) {
        getInstance(context).addInfoRequest(context);
    }

    @Deprecated
    public static void toggleNightMode(Context context) {
        action(context, Actions.INSTANCE.getNightMode().toggle());
    }

    /**
     * Get the authentication token from the API.
     *
     * @param context Context
     * @return Authentication token
     */
    private static String getToken(Context context) {
        return Preferences.getInstance(context).getToken();
    }

    /**
     * Add an authenticated action request to the API.
     *
     * @param context Context
     * @param action  Action
     */
    private void addRequest(Context context, String action) {
        requestQueue.add(new ActionRequest(action, response -> {
        }, this, getToken(context)));
    }

    private void addInfoRequest(Context context) {
        requestQueue.add(new InfoRequest(this, this, getToken(context)));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        EventBus.getDefault().post(new NetworkErrorEvent(error));
    }

    @Override
    public void onResponse(ResponseHolder response) {
        EventBus.getDefault().post(response);
    }
}
