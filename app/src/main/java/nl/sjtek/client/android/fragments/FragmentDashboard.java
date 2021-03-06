package nl.sjtek.client.android.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sjtek.client.android.R;

/**
 * Fragment that displays various {@link nl.sjtek.client.android.cards.BaseCard}s.
 */
public class FragmentDashboard extends BaseFragment {

    public FragmentDashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

}
