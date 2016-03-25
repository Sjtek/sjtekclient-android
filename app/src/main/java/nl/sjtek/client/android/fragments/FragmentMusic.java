package nl.sjtek.client.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.InfoRequest;
import nl.sjtek.client.android.interfaces.OnVolumePressListener;

/**
 * Created by Wouter Habets on 21-10-15.
 */
public class FragmentMusic extends BaseFragmentWeb implements OnVolumePressListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getUrl() {
        return "http://music.sjtek.nl/spotmop";
    }

    @Override
    public void onVolumeRaise() {
        addRequest(new InfoRequest(Action.Music.VOLUME_RAISE, this, this));
    }

    @Override
    public void onVolumeLower() {
        addRequest(new InfoRequest(Action.Music.VOLUME_LOWER, this, this));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_music, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                addRequest(new InfoRequest(Action.Music.SHUFFLE, this, this));
                return true;
            case R.id.action_clear:
                addRequest(new InfoRequest(Action.Music.CLEAR, this, this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
