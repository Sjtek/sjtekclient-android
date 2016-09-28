package nl.sjtek.client.android;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import nl.sjtek.client.android.utils.Storage;

/**
 * Created by Wouter Habets on 4-12-15.
 */
public class SjtekApp extends Application {

    static {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        Storage.getInstance(this);
    }
}
