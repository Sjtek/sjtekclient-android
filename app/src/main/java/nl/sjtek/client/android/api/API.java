package nl.sjtek.client.android.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;

import nl.sjtek.client.android.events.AuthFailedEvent;
import nl.sjtek.client.android.events.AuthSuccessfulEvent;
import nl.sjtek.client.android.events.MealEvent;
import nl.sjtek.client.android.events.NetworkErrorEvent;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.settings.DataCollection;

/**
 * Helper class for communicating with the API.
 */
public class API implements Response.Listener<ResponseCollection>, Response.ErrorListener {

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
        getInstance(context).requestQueue.add(new AuthenticationRequest(username, password, new Response.Listener<DataCollection>() {
            @Override
            public void onResponse(DataCollection response) {
                EventBus.getDefault().post(new AuthSuccessfulEvent(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EventBus.getDefault().post(new AuthFailedEvent(error));
            }
        }));
    }

    /**
     * Get the data (preferences) from the API.<br>
     * The acquired {@link DataCollection} will be send over the bus.<br>
     * This request is authenticated.
     *
     * @param context Context
     */
    public static void data(Context context) {
        API instance = getInstance(context);
        instance.requestQueue.add(new DataRequest(getToken(context),
                new Response.Listener<DataCollection>() {
                    @Override
                    public void onResponse(DataCollection response) {
                        EventBus.getDefault().post(response);
                    }
                }, instance));
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
    public static void led(Context context, int r, int g, int b) {
        getInstance(context).requestQueue.add(new LEDRequest(r, g, b, new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    /**
     * Send an action to the API with the given arguments.<br>
     * This will send {@link ResponseCollection} or {@link NetworkErrorEvent} on completion.<br>
     * This request is authenticated.
     *
     * @param context   Context
     * @param action    Action to send
     * @param arguments Arguments
     */
    public static void action(Context context, ActionInterface action, Arguments arguments) {
        getInstance(context).addRequest(context, action, arguments);
    }

    /**
     * Send an action to the API.<br>
     * This will send {@link ResponseCollection} or {@link NetworkErrorEvent} on completion.<br>
     * This request is authenticated.
     *
     * @param context Context
     * @param action  Action to send
     */
    public static void action(Context context, ActionInterface action) {
        action(context, action, Arguments.empty());
    }

    /**
     * Get the state from the API.<br>
     * This will send {@link ResponseCollection} or {@link NetworkErrorEvent} on completion.<br>
     * This request is authenticated.
     *
     * @param context Context
     */
    public static void info(Context context) {
        getInstance(context).addRequest(context, Action.REFRESH, Arguments.empty());
    }

    /**
     * Get a meal suggestion.<br>
     * This will send {@link MealEvent} over the bus.
     *
     * @param context Context
     */
    public static void meal(Context context) {
        getInstance(context).requestQueue.add(new MealRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                EventBus.getDefault().post(new MealEvent(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EventBus.getDefault().post(new MealEvent(""));
            }
        }));
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
     * @param context   Context
     * @param action    Action
     * @param arguments Arguments
     */
    private void addRequest(Context context, ActionInterface action, Arguments arguments) {
        requestQueue.add(new InfoRequest(action.toString() + arguments.build(), this, this, getToken(context)));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        EventBus.getDefault().post(new NetworkErrorEvent(error));
    }

    @Override
    public void onResponse(ResponseCollection response) {
        EventBus.getDefault().post(response);
    }
}
