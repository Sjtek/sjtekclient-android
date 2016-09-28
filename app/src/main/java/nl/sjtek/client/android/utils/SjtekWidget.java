package nl.sjtek.client.android.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.widget.RemoteViews;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.activities.ActivityMain;
import nl.sjtek.client.android.services.CommandService;

/**
 * Created by Wouter Habets on 30-12-15.
 */
public class SjtekWidget {

    public SjtekWidget() {

    }

    public static RemoteViews getWidget(Context context, boolean widget) {
        Intent viewIntent = new Intent(context, ActivityMain.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentMusicToggle = new Intent(context, CommandService.class);
        intentMusicToggle.setAction(context.getString(R.string.service_action_music_toggle));
        PendingIntent pendingIntentMusicToggle = PendingIntent.getService(context, 10, intentMusicToggle, 0);

        Intent intentMusicNext = new Intent(context, CommandService.class);
        intentMusicNext.setAction(context.getString(R.string.service_action_music_next));
        PendingIntent pendingIntentMusicNext = PendingIntent.getService(context, 20, intentMusicNext, 0);

        Intent intentSwitch = new Intent(context, CommandService.class);
        intentSwitch.setAction(context.getString(R.string.service_action_switch));
        PendingIntent pendingIntentSwitch = PendingIntent.getService(context, 30, intentSwitch, 0);

        @LayoutRes int layoutRes;
        if (widget) {
            if (PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(context.getString(R.string.pref_key_widget_transparent), false)) {
                layoutRes = R.layout.widget_transparent;
            } else {
                layoutRes = R.layout.widget;
            }
        } else {
            layoutRes = R.layout.notification;
        }

        RemoteViews view = new RemoteViews(context.getPackageName(), layoutRes);
        view.setOnClickPendingIntent(R.id.buttonApp, viewPendingIntent);
        view.setOnClickPendingIntent(R.id.buttonPlay, pendingIntentMusicToggle);
        view.setOnClickPendingIntent(R.id.buttonNext, pendingIntentMusicNext);
        view.setOnClickPendingIntent(R.id.buttonToggle, pendingIntentSwitch);
        return view;
    }
}
