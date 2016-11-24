package nl.sjtek.client.android.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.storage.Preferences;

public class WeatherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        Preferences preferences = Preferences.getInstance(getActivity());
        ((TextView) view.findViewById(R.id.textViewTempIn)).setText(String.valueOf(preferences.getInside()));
        ((TextView) view.findViewById(R.id.textViewTempOut)).setText(String.valueOf(preferences.getOutside()));
        return view;
    }
}
