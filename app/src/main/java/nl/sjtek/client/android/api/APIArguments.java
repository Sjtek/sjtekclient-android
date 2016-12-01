package nl.sjtek.client.android.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;
import nl.sjtek.control.data.actions.Arguments;


/**
 * Class for building URL parameters for API calls.
 */
public class APIArguments extends nl.sjtek.control.data.actions.Arguments {

    @Override
    public APIArguments setUseVoice(boolean useVoice) {
        super.setUseVoice(useVoice);
        return this;
    }

    @Override
    public APIArguments setUser(String user) {
        super.setUser(user);
        return this;
    }

    @Override
    public APIArguments setUrl(String url) {
        super.setUrl(url);
        return this;
    }

    /**
     * Set the user according to the currently signed in user.
     *
     * @param context Context for getting the user
     * @return Arguments
     */
    @SuppressLint("DefaultLocale")
    public Arguments setDefaultUser(Context context) {
        String user = Preferences.getInstance(context).getUsername();
        if (TextUtils.isEmpty(user)) return this;
        setUser(user.toLowerCase());
        return this;
    }

    /**
     * Set the playlist to the user default playlist.
     *
     * @param context Context for getting the playlist
     * @return Arguments
     */
    public Arguments setDefaultPlaylist(Context context) {
        setUrl(StateManager.getInstance(context).getDefaultPlaylist(context));
        return this;
    }
}
