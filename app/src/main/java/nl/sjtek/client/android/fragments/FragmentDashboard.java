package nl.sjtek.client.android.fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.activities.ActivityMain;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.UpdateRequest;
import nl.sjtek.client.android.update.Music;
import nl.sjtek.client.android.update.Update;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends BaseFragment implements View.OnClickListener {

    private static final long REFRESH_INTERVAL = 1000;

    private Holder holder;
    private CountDownTimer updateTimer = new CountDownTimer(REFRESH_INTERVAL, REFRESH_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (!areRequestsRunning())
                addRequest(new UpdateRequest(FragmentDashboard.this, FragmentDashboard.this));

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
        initOnClickListeners();
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

    private void initOnClickListeners() {
        View.OnClickListener switchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchClick(v);
            }
        };

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMusicButtonClick(v);
            }
        };

        View.OnClickListener musicCardClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityMain) getActivity()).replaceFragment(new FragmentMusic(), true);
            }
        };

        holder.switchLight1.setOnClickListener(switchClickListener);
        holder.switchLight2.setOnClickListener(switchClickListener);
        holder.switchLight3.setOnClickListener(switchClickListener);

        holder.buttonMusicToggle.setOnClickListener(buttonClickListener);
        holder.buttonMusicNext.setOnClickListener(buttonClickListener);

        holder.viewCardMusic.setOnClickListener(musicCardClickListener);
    }

    public void onSwitchClick(View v) {
        Action.Light action = null;
        boolean isChecked = ((Switch) v).isChecked();
        switch (v.getId()) {
            case R.id.switchLights1:
                action = (!isChecked ? Action.Light.TOGGLE_1_OFF : Action.Light.TOGGLE_1_ON);
                break;
            case R.id.switchLights2:
                action = (!isChecked ? Action.Light.TOGGLE_2_OFF : Action.Light.TOGGLE_2_ON);
                break;
            case R.id.switchLights3:
                action = (!isChecked ? Action.Light.TOGGLE_3_OFF : Action.Light.TOGGLE_3_ON);
                break;
        }

        if (action != null) addRequest(new UpdateRequest(action, this, this));
    }

    public void onMusicButtonClick(View v) {
        Action.Music action = null;
        switch (v.getId()) {
            case R.id.buttonMusicToggle:
                action = Action.Music.TOGGLE;
                break;
            case R.id.buttonMusicNext:
                action = Action.Music.NEXT;
                break;
        }

        if (action != null) addRequest(new UpdateRequest(action, this, this));
    }

    @Override
    public void onClick(View v) {
        onSwitchClick(v);
        onMusicButtonClick(v);
    }

    @Override
    protected void onUpdate(Update update) {
        super.onUpdate(update);

        if (getView() == null) return;

        if (update.getMusic().getStatus() == Music.PlayerStatus.STATUS_PLAYING) {
            holder.buttonMusicToggle.setImageResource(R.drawable.ic_action_pause);
        } else {
            holder.buttonMusicToggle.setImageResource(R.drawable.ic_action_play);
        }

        if (update.getMusic().getStatus() == Music.PlayerStatus.STATUS_STOPPED) {
            holder.textViewMusicTitle.setVisibility(View.VISIBLE);
            holder.textViewMusicArtist.setVisibility(View.INVISIBLE);
            holder.textViewMusicTimeElapsed.setVisibility(View.INVISIBLE);
            holder.textViewMusicTimeTotal.setVisibility(View.INVISIBLE);
            holder.progressBarMusic.setVisibility(View.INVISIBLE);

            holder.textViewMusicTitle.setText("Music stopped");
        } else {
            holder.textViewMusicTitle.setVisibility(View.VISIBLE);
            holder.textViewMusicArtist.setVisibility(View.VISIBLE);
            holder.textViewMusicTimeElapsed.setVisibility(View.VISIBLE);
            holder.textViewMusicTimeTotal.setVisibility(View.VISIBLE);
            holder.progressBarMusic.setVisibility(View.VISIBLE);

            int timeElapsed = update.getMusic().getSong().getElapsed();
            int timeTotal = update.getMusic().getSong().getTotal();

            holder.textViewMusicTitle.setText(update.getMusic().getSong().getTitle());
            holder.textViewMusicArtist.setText(update.getMusic().getSong().getArtist());
            holder.textViewMusicTimeElapsed.setText(String.format("%02d:%02d", timeElapsed / 60, timeElapsed % 60));
            holder.textViewMusicTimeTotal.setText(String.format("%02d:%02d", timeTotal / 60, timeTotal % 60));

            holder.progressBarMusic.setMax(timeTotal);
            holder.progressBarMusic.setProgress(timeElapsed);
        }

        holder.textViewTempInside.setText(String.format("%01d C°", update.getTemperature().getInside()));
        holder.textViewTempOutside.setText(String.format("%01d C°", update.getTemperature().getOutside()));

        if (areRequestsRunning()) return;
        holder.switchLight1.setChecked(update.getLights().isUnit1());
        holder.switchLight2.setChecked(update.getLights().isUnit2());
        holder.switchLight3.setChecked(update.getLights().isUnit3());
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

    private class Holder {
        private Switch switchLight1;
        private Switch switchLight2;
        private Switch switchLight3;

        private View viewCardMusic;

        private ImageButton buttonMusicToggle;
        private ImageButton buttonMusicNext;

        private TextView textViewMusicTitle;
        private TextView textViewMusicArtist;
        private TextView textViewMusicTimeElapsed;
        private TextView textViewMusicTimeTotal;
        private ProgressBar progressBarMusic;

        private TextView textViewTempInside;
        private TextView textViewTempOutside;

        public Holder(View view) {
            switchLight1 = (Switch) view.findViewById(R.id.switchLights1);
            switchLight2 = (Switch) view.findViewById(R.id.switchLights2);
            switchLight3 = (Switch) view.findViewById(R.id.switchLights3);

            viewCardMusic = view.findViewById(R.id.cardMusic);

            buttonMusicToggle = (ImageButton) view.findViewById(R.id.buttonMusicToggle);
            buttonMusicNext = (ImageButton) view.findViewById(R.id.buttonMusicNext);

            textViewMusicTitle = (TextView) view.findViewById(R.id.textViewMusicTitle);
            textViewMusicArtist = (TextView) view.findViewById(R.id.textViewMusicArtist);
            textViewMusicTimeElapsed = (TextView) view.findViewById(R.id.textViewTimeElapsed);
            textViewMusicTimeTotal = (TextView) view.findViewById(R.id.textViewTimeTotal);
            progressBarMusic = (ProgressBar) view.findViewById(R.id.progressBar);

            textViewTempInside = (TextView) view.findViewById(R.id.textViewTempInside);
            textViewTempOutside = (TextView) view.findViewById(R.id.textViewTempOutside);
        }
    }
}
