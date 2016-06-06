package nl.sjtek.client.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import nl.sjtek.client.android.SjtekApp;

/**
 * Created by Wouter Habets on 4-12-15.
 */
public class Storage {

    private static final String SHARED_PREFERENCES_NAME = "shared_preferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EXTRA_LIGHTS = "extra_lights";
    private static final String KEY_PLAYLIST = "playlist";

    private static Storage instance = new Storage();
    private SharedPreferences sharedPreferences;

    private Storage() {
        Context context = SjtekApp.getContext();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Storage getInstance() {
        return instance;
    }

    public void clearCredentials() {
        setCredentials("", "");
        setCheckExtraLights(false);
    }

    public void setCredentials(String username, String password) {
        sharedPreferences.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_PASSWORD, password)
                .apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public boolean isCredentialsSet() {
        return (!getUsername().isEmpty() && !getPassword().isEmpty());
    }

    public String getCredentials() {
        String credentials = String.format("%s:%s", getUsername(), getPassword());
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
    }

    public boolean getCheckExtraLights() {
        return sharedPreferences.getBoolean(KEY_EXTRA_LIGHTS, false);
    }

    public void setCheckExtraLights(boolean doCheck) {
        sharedPreferences.edit()
                .putBoolean(KEY_EXTRA_LIGHTS, doCheck)
                .apply();
    }

    public String getDefaultPlaylist() {
        return sharedPreferences.getString(KEY_PLAYLIST, null);
    }

    public void setDefaultPlaylist(String playlist) {
        sharedPreferences.edit()
                .putString(KEY_PLAYLIST, playlist)
                .apply();
    }
}
