package nl.sjtek.client.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.utils.SjtekWidget;

public class WiFiReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1001;

    public WiFiReceiver() {
    }

    public static void updateNotification(Context context) {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        String ssid = wifiInfo.getSSID();
//
//        ConnectivityManager cm =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//        boolean isWiFi = (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI);
//
//        if (isWiFi && ssid != null && (ssid.contains("Routers of Rohan") || ssid.contains("Routers of Rohan - 5GHz"))) {
//            dismissNotification(context);
//        } else {
//            showNotification(context);
//        }
        showNotification(context);
    }

    private static void showNotification(Context context) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_black_24dp)
                        .setContent(SjtekWidget.getWidget(context, false))
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
