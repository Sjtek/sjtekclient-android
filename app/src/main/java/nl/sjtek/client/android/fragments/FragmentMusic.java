package nl.sjtek.client.android.fragments;

import nl.sjtek.client.android.interfaces.OnVolumePressListener;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.UpdateRequest;

/**
 * Created by Wouter Habets on 21-10-15.
 */
public class FragmentMusic extends BaseFragmentWeb implements OnVolumePressListener {

    @Override
    protected String getUrl() {
        return "http://sjtek.nl/music";
    }

    @Override
    public void onVolumeRaise() {
        addRequest(new UpdateRequest(Action.Music.VOLUME_RAISE, this, this));
    }

    @Override
    public void onVolumeLower() {
        addRequest(new UpdateRequest(Action.Music.VOLUME_LOWER, this, this));
    }
}
