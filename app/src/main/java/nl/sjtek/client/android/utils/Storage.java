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
    }

    public void setCredentials(String username, String pasword) {
        sharedPreferences.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_PASSWORD, pasword)
                .apply();
    }

    private String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    private String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public boolean isCredentialsSet() {
        return (!getUsername().isEmpty() && !getPassword().isEmpty());
    }

    public String getCredentials() {
        String credentials = String.format("%s:%s", getUsername(), getPassword());
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
    }
}
