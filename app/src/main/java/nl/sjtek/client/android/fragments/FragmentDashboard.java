package nl.sjtek.client.android.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.ActionInterface;
import nl.sjtek.client.android.api.InfoRequest;
import nl.sjtek.client.android.cards.BaseCard;
import nl.sjtek.client.android.cards.UserCard;
import nl.sjtek.client.android.events.ConnectionEvent;
import nl.sjtek.client.android.services.SjtekService;
import nl.sjtek.client.android.utils.Storage;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends BaseFragment {

    private Holder holder;

    public FragmentDashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        holder = new Holder(rootView);
        EventBus.getDefault().register(this);
        getContext().startService(new Intent(getContext(), SjtekService.class));
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        getContext().stopService(new Intent(getContext(), SjtekService.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketUpdate(ResponseCollection responseCollection) {
        onUpdate(responseCollection);
    }

    @Override
    protected void onUpdate(ResponseCollection update) {
        super.onUpdate(update);
        if (holder != null && !areRequestsRunning()) holder.onUpdate(update);
    }

    @Subscribe
    public void onConnectionChanged(ConnectionEvent event) {
        if (getView() != null && !event.isConnected())
            Snackbar.make(getView(), "Disconnected", Snackbar.LENGTH_SHORT).show();
    }

    private class Holder implements BaseCard.OnClickListener {

        private UserCard userCard;
        private BaseCard lightsCard;
        private BaseCard musicCard;
        private BaseCard temperatureCard;

        public Holder(View view) {
            userCard = (UserCard) view.findViewById(R.id.userCard);
            userCard.setOnClickListener(this);
            lightsCard = (BaseCard) view.findViewById(R.id.lightsCard);
            lightsCard.setOnClickListener(this);
            musicCard = (BaseCard) view.findViewById(R.id.musicCard);
            musicCard.setOnClickListener(this);
            temperatureCard = (BaseCard) view.findViewById(R.id.temperatureCard);
            temperatureCard.setOnClickListener(this);
        }

        public void onUpdate(ResponseCollection update) {
            lightsCard.onUpdate(update);
            musicCard.onUpdate(update);
            temperatureCard.onUpdate(update);
        }

        @Override
        public void onClick(ActionInterface action, String paramUrl) {
            String urlString = action.getUrl();
            if (paramUrl != null) {
                urlString += "?url=" + paramUrl;
            }
            addRequest(new InfoRequest(urlString, FragmentDashboard.this, FragmentDashboard.this, Storage.getInstance(getActivity()).getCredentials()));
        }
    }
}
