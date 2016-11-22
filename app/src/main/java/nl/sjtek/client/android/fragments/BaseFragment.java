package nl.sjtek.client.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nl.sjtek.client.android.activities.ActivityLogin;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.events.NetworkErrorEvent;

/**
 * Base fragment that displays network error Snackbars.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        API.info(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCannotConnect(NetworkErrorEvent event) {
        // TODO: 22-11-16 Remove hardcoded strings
        if (getView() == null) return;
        Snackbar snackbar = Snackbar.make(getView(), event.getMessage(), Snackbar.LENGTH_INDEFINITE)
                .setAction("Refresh", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        API.info(getContext());
                    }
                });
        if (event.isShowSignIn()) snackbar.setAction("Sign in", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityLogin.class));
            }
        });

        snackbar.show();
    }
}
