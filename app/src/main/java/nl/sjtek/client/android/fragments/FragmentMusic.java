package nl.sjtek.client.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;

/**
 * Fragment for the Mopidy web interface.
 * Displays extra toolbar action buttons for controlling Mopidy.
 */
public class FragmentMusic extends BaseFragmentWeb {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getUrl() {
        return "http://music.sjtek.nl/musicbox_webclient/index.html#home";
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
                API.action(getContext(), Action.Music.SHUFFLE);
                return true;
            case R.id.action_clear:
                API.action(getContext(), Action.Music.CLEAR);
                return true;
            case R.id.action_start:
                API.action(getContext(), Action.Music.START, new Arguments().setDefaultPlaylist(getContext()));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
