package nl.sjtek.client.android.fragments;

import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.InfoRequest;
import nl.sjtek.client.android.interfaces.OnVolumePressListener;

/**
 * Created by Wouter Habets on 21-10-15.
 */
public class FragmentMusic extends BaseFragmentWeb implements OnVolumePressListener {

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
}
