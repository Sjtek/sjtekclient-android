package nl.sjtek.client.android.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

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

    public static boolean isEmpty(String s) {
        return s == null || s.length() <= 0 || s.trim().length() <= 0;
    }

    public void clearCredentials() {
        setCredentials("", "");
    }

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

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public String getUsername() {
        if (isEmpty(getToken())) return "";
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public boolean isCredentialsSet() {
        return (!isEmpty(getUsername()) && !isEmpty(getToken()));
    }

    public boolean areCredentialsChanged() {
        return sharedPreferences.getBoolean(KEY_CREDENTIALS_CHANGED, false);
    }

    public void clearCredentialsChangedFlag() {
        sharedPreferences.edit()
                .putBoolean(KEY_CREDENTIALS_CHANGED, false)
                .apply();
    }
}
