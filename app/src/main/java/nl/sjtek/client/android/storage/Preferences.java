package nl.sjtek.client.android.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Base64;

import nl.sjtek.client.android.R;

/**
 * Helper for {@link SharedPreferences}.
 */
public class Preferences {

    private static final String SHARED_PREFERENCES_NAME = "shared_preferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_CREDENTIALS_CHANGED = "credentials_changed";

    private static Preferences instance;
    private final SharedPreferences sharedPreferences;

    private Preferences(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    Preferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public static synchronized Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context.getApplicationContext());
        }
        return instance;
    }

    public static boolean areNotificationEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getBoolean(context.getString(R.string.pref_key_notification_enable), true);
    }

    /**
     * Check if a String is empty.
     *
     * @param s String or null
     * @return Is the string empty
     */
    public static boolean isEmpty(@Nullable String s) {
        return s == null || s.length() <= 0 || s.trim().length() <= 0;
    }

    /**
     * Clear the user credentials and set the credentials changed flag.
     */
    public void clearCredentials() {
        setCredentials("", "");
    }

    /**
     * Set the user credentials.
     *
     * @param username Username
     * @param password Password
     */
    public void setCredentials(String username, String password) {
        String token;
        if (isEmpty(username) && isEmpty(password)) {
            token = "";
        } else {
            String credentials = String.format("%s:%s", username, password);
            token = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        }

        sharedPreferences.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_TOKEN, token)
                .putBoolean(KEY_CREDENTIALS_CHANGED, true)
                .apply();
    }

    /**
     * Get the authentication token. Is empty if no credentials are set.
     *
     * @return Credentials
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    /**
     * Get the username. Is empty if no credentials are set.
     *
     * @return Username
     */
    public String getUsername() {
        if (isEmpty(getToken())) return "";
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    /**
     * Check if the credentials are set.
     *
     * @return Are credentials set
     */
    public boolean isCredentialsSet() {
        return (!isEmpty(getUsername()) && !isEmpty(getToken()));
    }

    /**
     * Check if the credentials are changed.
     *
     * @return Are credentials changed
     */
    public boolean areCredentialsChanged() {
        return sharedPreferences.getBoolean(KEY_CREDENTIALS_CHANGED, false);
    }

    /**
     * Clear the credentials changed flag.
     */
    public void clearCredentialsChangedFlag() {
        sharedPreferences.edit()
                .putBoolean(KEY_CREDENTIALS_CHANGED, false)
                .apply();
    }
}
