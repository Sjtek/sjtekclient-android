package nl.sjtek.client.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import nl.sjtek.client.android.activities.ActivityLogin;
import nl.sjtek.client.android.api.InfoRequest;
import nl.sjtek.control.data.responses.ResponseCollection;

public abstract class BaseFragment extends Fragment implements
        Response.Listener<ResponseCollection>,
        Response.ErrorListener {

    private static final int MAX_RETRIES = 1;

    private RequestQueue requestQueue;
    private int runningRequests = 0;
    private int requestErrors = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        requestQueue = Volley.newRequestQueue(context);
        addRequest(new InfoRequest(this, this));
    }

    protected void addRequest(InfoRequest request) {
        runningRequests++;
        request.setTag(this);
        requestQueue.add(request);
    }

    protected void resetConnectionCounters() {
        requestQueue.cancelAll(this);
        runningRequests = 0;
        requestErrors = 0;
    }

    protected boolean areRequestsRunning() {
        return (runningRequests != 0);
    }

    protected int getRunningRequestsCount() {
        return runningRequests;
    }

    @Override
    public void onResponse(ResponseCollection response) {
        runningRequests--;
        requestErrors = 0;
        if (runningRequests == 0) onUpdate(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        runningRequests--;
        requestErrors++;
        onError(error);
        if (requestErrors >= MAX_RETRIES) {
            onCannotConnect(error);
        }
    }

    protected void onUpdate(ResponseCollection update) {

    }

    protected void onError(VolleyError error) {

    }

    protected void onRetry() {
        resetConnectionCounters();
    }

    protected void onCannotConnect(VolleyError error) {
        if (getView() == null) return;
        boolean showSignIn = false;
        String message;
        if (error instanceof NoConnectionError) {
            message = "Oops, no connection.";
        } else if (error instanceof TimeoutError) {
            message = "Oops, request timed out.";
        } else if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
            message = "Oops, you need to be signed in to use this app outside the Sjtek.";
            showSignIn = true;
        } else if (error.networkResponse != null) {
            message = "Error: " + error.networkResponse.statusCode + " - " + error.getMessage();
        } else {
            message = "Error: " + error.getMessage();
        }
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRetry();
                    }
                });
        if (showSignIn) snackbar.setAction("Sign in", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityLogin.class));
            }
        });

        snackbar.show();
    }

    protected void goToFragment(Fragment fragment) {

    }
}
