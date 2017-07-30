package nl.sjtek.client.android.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.utils.SjtekWidget;

/**
 * Broadcast receiver for WiFi connection changes. Handles mainly the shortcuts notification.
 */
public class WiFiReceiver extends BroadcastReceiver {

    private static final String SSID = "Internetjes";
    private static final int NOTIFICATION_ID = 1001;
    private static final String NOTIFICATION_CHANNEL = "wifi-notification";

    public WiFiReceiver() {
    }

    /**
     * Show or hide the shortcuts notification bases on the WiFi status and user preference.
     *
     * @param context Context
     */
    public static void updateNotification(Context context) {
        if (!PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getBoolean(context.getString(R.string.pref_key_notification_enable), true)) {
            dismissNotification(context);
            return;
        }
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (ssid != null && (ssid.contains(SSID))) {
            showNotification(context);
        } else {
            dismissNotification(context);
        }
    }

    private static void showNotification(Context context) {
        int icon;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            icon = R.drawable.ic_notification_drawable;
        } else {
            icon = R.drawable.ic_notification_black_24dp;
        }

        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context);
        } else {
            createNotificationChannel(context);
            builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL);
        }

        builder.setSmallIcon(icon)
                .setContent(SjtekWidget.getWidget(context, false))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static void dismissNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private static void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence name = context.getString(R.string.notification_channel_name);
        String description = context.getString(R.string.notification_channel_description);
        int importance = NotificationManager.IMPORTANCE_MIN;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
        channel.setDescription(description);
        channel.enableLights(false);
        channel.enableVibration(false);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) return;

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {
            updateNotification(context);
        } else {
            dismissNotification(context);
        }
    }
}
