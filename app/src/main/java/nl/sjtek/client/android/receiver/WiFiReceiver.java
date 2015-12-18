package nl.sjtek.client.android.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.SjtekApp;
import nl.sjtek.client.android.activities.ActivityMain;
import nl.sjtek.client.android.services.CommandService;

public class WiFiReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1001;

    public WiFiReceiver() {
    }

    public static void updateNotification(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
//        if (ssid != null &&
//                (ssid.contains("Routers of Rohan") || ssid.contains("Routers of Rohan - 5GHz"))) {
//            showNotification(context);
//        } else {
//            dismissNotification(context);
//        }
        showNotification(context);
    }

    private static void showNotification(Context context) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_black_24dp)
                        .setContent(getRemoteView(context))
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static void dismissNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private static RemoteViews getRemoteView(Context context) {
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

        RemoteViews view = new RemoteViews(SjtekApp.getContext().getPackageName(), R.layout.notification);
        view.setOnClickPendingIntent(R.id.buttonApp, viewPendingIntent);
        view.setOnClickPendingIntent(R.id.buttonPlay, pendingIntentMusicToggle);
        view.setOnClickPendingIntent(R.id.buttonNext, pendingIntentMusicNext);
        view.setOnClickPendingIntent(R.id.buttonToggle, pendingIntentSwitch);
        return view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {
            updateNotification(context);
        } else {
            dismissNotification(context);
        }
    }
}
