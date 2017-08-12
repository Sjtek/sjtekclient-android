package nl.sjtek.client.android.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import nl.sjtek.client.android.R

/**
 * Created by wouter on 12-8-17.
 */
object NotificationUtils {

    const val NOTIFICATION_ID = 1001
    private const val NOTIFICATION_CHANNEL = "wifi-notification"

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = context.getString(R.string.notification_channel_name)
        val description = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_MIN
        val channel = NotificationChannel(NOTIFICATION_CHANNEL, name, importance)
        channel.description = description
        channel.enableLights(false)
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
    }

    fun getNotification(context: Context): Notification {
        val icon: Int
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            icon = R.drawable.ic_notification_drawable
        } else {
            icon = R.drawable.ic_notification_black_24dp
        }

        createNotificationChannel(context)
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(icon)
                .setContent(SjtekWidget.getWidget(context, false))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setOngoing(true)
                .build()
    }
}