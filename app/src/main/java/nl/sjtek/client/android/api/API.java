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
 * Created by wouter on 9-11-16.
 */

public class API implements Response.Listener<ResponseCollection>, Response.ErrorListener {

    private static API instance;
    private RequestQueue requestQueue;

    private API(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    private static synchronized API getInstance(Context context) {
        if (instance == null) {
            instance = new API(context);
        }

        return instance;
    }

    public static void init(Context context) {
        getInstance(context);
    }

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

    public static void action(Context context, ActionInterface action, Arguments arguments) {
        getInstance(context).addRequest(context, action, arguments);
    }

    public static void action(Context context, ActionInterface action) {
        action(context, action, Arguments.empty());
    }

    public static void info(Context context) {
        getInstance(context).addRequest(context, Action.REFRESH, Arguments.empty());
    }

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

    private void addRequest(Context context, ActionInterface action, Arguments arguments) {
        requestQueue.add(new InfoRequest(action.toString() + arguments.build(), this, this, Preferences.getInstance(context).getCredentials()));
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
