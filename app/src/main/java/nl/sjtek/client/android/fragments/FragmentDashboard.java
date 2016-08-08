package nl.sjtek.client.android.fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.ActionInterface;
import nl.sjtek.client.android.api.InfoRequest;
import nl.sjtek.client.android.cards.BaseCard;
import nl.sjtek.client.android.cards.UserCard;
import nl.sjtek.client.android.utils.Storage;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends BaseFragment {

    private static final long REFRESH_INTERVAL = 1000;

    private Holder holder;

    private CountDownTimer updateTimer = new CountDownTimer(REFRESH_INTERVAL, REFRESH_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (!areRequestsRunning() && getActivity() != null)
                addRequest(new InfoRequest(FragmentDashboard.this, FragmentDashboard.this, Storage.getInstance(getActivity()).getCredentials()));

            updateTimer.start();
        }
    };

    public FragmentDashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        holder = new Holder(rootView);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTimer.start();
    }

    @Override
    protected void onError(VolleyError error) {
        super.onError(error);
    }

    @Override
    protected void onCannotConnect(VolleyError error) {
        super.onCannotConnect(error);
        updateTimer.cancel();
    }

    @Override
    protected void onRetry() {
        super.onRetry();
        updateTimer.start();
    }

    @Override
    protected void onUpdate(ResponseCollection update) {
        super.onUpdate(update);
        if (holder != null && !areRequestsRunning()) holder.onUpdate(update);
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
