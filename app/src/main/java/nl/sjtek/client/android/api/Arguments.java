package nl.sjtek.client.android.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;


/**
 * Class for building URL parameters for API calls.
 */
public class Arguments implements Serializable {

    private static final String ENCODING = "UTF-8";

    private boolean useVoice;
    private String user;
    private String url;

    public Arguments() {

    }

    /**
     * Get empty arguments.
     *
     * @return Empty arguments
     */
    public static Arguments empty() {
        return new Arguments();
    }

    /**
     * Should use voice with the call.
     *
     * @return Use voice
     */
    public boolean isUseVoice() {
        return useVoice;
    }

    /**
     * Let the API speak in the living room on receiving a certain request.
     *
     * @param useVoice Should use voice
     * @return Arguments
     */
    public Arguments setUseVoice(boolean useVoice) {
        this.useVoice = useVoice;
        return this;
    }

    /**
     * Get the user.
     *
     * @return User
     */
    public String getUser() {
        try {
            return URLDecoder.decode(user, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set the user for identifying with the API.
     *
     * @param user User
     * @return Arguments
     */
    public Arguments setUser(String user) {
        if (user == null) user = "";
        try {
            this.user = URLEncoder.encode(user, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
     * Get the url for starting a specific playlist/song.
     *
     * @return Url of the playlist/song
     */
    public String getUrl() {
        try {
            return URLDecoder.decode(url, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set an url for a music call to specifically start a playlist/song.
     *
     * @param url Url of the playlist/song.
     * @return Arguments
     */
    public Arguments setUrl(String url) {
        if (url == null) url = "";
        try {
            this.url = URLEncoder.encode(url, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

    /**
     * Build the arguments to a url encoded string for the url.
     *
     * @return Url encoded arguments
     */
    public String build() {
        List<String> argumentsList = new ArrayList<>();
        if (useVoice) argumentsList.add("voice");
        if (!TextUtils.isEmpty(url)) argumentsList.add("url=" + url);
        argumentsList.add("user=" + (TextUtils.isEmpty(user) ? "default" : user));
        return "?" + TextUtils.join("&", argumentsList);
    }

    @Override
    /**
     * {@see #build()}
     */
    public String toString() {
        return build();
    }
}
