package nl.sjtek.client.android.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import nl.sjtek.client.android.R;
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
        if (ssid != null &&
                (ssid.contains("Routers of Rohan") || ssid.contains("Routers of Rohan - 5GHz"))) {
            showNotification(context);
        } else {
            dismissNotification(context);
        }
    }

    private static void showNotification(Context context) {
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

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .setBackground(BitmapFactory.decodeResource(context.getResources(), R.drawable.alexander));

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("SjtekControl")
                        .setContentText("Connected to SjtekControl")
                        .setContentIntent(viewPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setOngoing(true)
                        .extend(wearableExtender)

                        .addAction(R.drawable.ic_play_arrow_black_24dp, "Play/Pause", pendingIntentMusicToggle)
                        .addAction(R.drawable.ic_skip_next_black_24dp, "Next", pendingIntentMusicNext)
                        .addAction(R.drawable.ic_power_black_24dp, "Toggle", pendingIntentSwitch);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static void dismissNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_ID);
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
