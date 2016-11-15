package nl.sjtek.client.android;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.utils.ShortcutUtils;

/**
 * Created by Wouter Habets on 4-12-15.
 */
public class SjtekApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        Preferences.getInstance(this);
        ShortcutUtils.setShortcuts(this);
        API.init(this);
    }
}
