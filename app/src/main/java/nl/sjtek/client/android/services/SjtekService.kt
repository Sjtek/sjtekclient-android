package nl.sjtek.client.android.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import nl.sjtek.client.android.receiver.SleepReceiver
import nl.sjtek.client.android.utils.NotificationUtils

class SjtekService : Service() {

    private val sleepFilter = SleepReceiver.filter
    private val sleepReceiver = SleepReceiver()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(SERVICE_ID, NotificationUtils.getNotification(this))
        registerReceiver(sleepReceiver, sleepFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(sleepReceiver)
    }

    companion object {
        private val SERVICE_ID = 10
    }
}
