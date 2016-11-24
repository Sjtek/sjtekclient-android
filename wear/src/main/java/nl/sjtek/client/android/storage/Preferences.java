package nl.sjtek.client.android.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper for the {@link SharedPreferences} class.
 */
public class Preferences {
    private static final String KEY = "preferences";
    private static final String KEY_INSIDE = "inside";
    private static final String KEY_OUTSIDE = "outside";
    private static Preferences instance;
    private SharedPreferences sharedPreferences;

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static synchronized Preferences getInstance(Context context) {
        if (instance == null) instance = new Preferences(context);
        return instance;
    }

    public void setTemperature(int inside, int outside) {
        sharedPreferences.edit()
                .putInt(KEY_INSIDE, inside)
                .putInt(KEY_OUTSIDE, outside)
                .apply();
    }

    public int getOutside() {
        return sharedPreferences.getInt(KEY_OUTSIDE, 0);
    }

    public int getInside() {
        return sharedPreferences.getInt(KEY_INSIDE, 0);
    }
}
