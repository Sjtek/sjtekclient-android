package nl.sjtek.client.android;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;

import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.geofence.GeofenceUtils;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;
import nl.sjtek.client.android.utils.ShortcutUtils;

/**
 * Sjtek
 */
public class SjtekApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        StateManager.INSTANCE.load(this);
        Preferences.getInstance(this);
        ShortcutUtils.setShortcuts(this);
        API.init(this);
        GeofenceUtils.INSTANCE.start(this);
    }
}
