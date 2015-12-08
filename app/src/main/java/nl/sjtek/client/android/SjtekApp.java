package nl.sjtek.client.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by Wouter Habets on 4-12-15.
 */
public class SjtekApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
