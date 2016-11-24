package nl.sjtek.client.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.ActionInterface;
import nl.sjtek.client.android.api.ActionSender;

public class LightsFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lights, container, false);
        view.findViewById(R.id.buttonToggle).setOnClickListener(this);
        view.findViewById(R.id.buttonPlayPause).setOnClickListener(this);
        view.findViewById(R.id.buttonLightsOff).setOnClickListener(this);
        view.findViewById(R.id.buttonLightsOn).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonToggle:
                sendCommand(Action.SWITCH);
                break;
            case R.id.buttonPlayPause:
                sendCommand(Action.Music.TOGGLE);
                break;
            case R.id.buttonLightsOff:
                sendCommand(Action.Light.TOGGLE_1_OFF);
                sendCommand(Action.Light.TOGGLE_2_OFF);
                break;
            case R.id.buttonLightsOn:
                sendCommand(Action.Light.TOGGLE_1_ON);
                sendCommand(Action.Light.TOGGLE_2_ON);
                break;
        }
    }

    private void sendCommand(ActionInterface action) {
        ((ActionSender) getActivity()).sendAction(action);
    }
}
