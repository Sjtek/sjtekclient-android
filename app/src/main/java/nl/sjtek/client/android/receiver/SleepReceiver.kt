package nl.sjtek.client.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import nl.sjtek.client.android.api.API
import nl.sjtek.control.data.actions.Actions

class SleepReceiver : BroadcastReceiver() {

    private fun isInSjtek(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val currentSSID = wifiManager.connectionInfo.ssid
        return SSID == currentSSID
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (!isInSjtek(context)) return
        when (intent.action) {
            ACTION_START -> start(context)
            ACTION_STOP -> stop(context)
            ACTION_SNOOZE -> snooze(context)
            ACTION_ALARM_TRIGGERED -> alarmTriggered(context)
            ACTION_ALARM_DISMISSED -> alarmDismissed(context)
            ACTION_SMART_PERIOD -> smartPeriod(context)
        }
    }

    private fun start(context: Context) {
        API.action(context, Actions.nightMode.enable())
        API.action(context, Actions.lights.toggle(room = "wouter", enabled = false))
        setSyncthing(context, true)
    }

    private fun stop(context: Context) {
        setSyncthing(context, false)
    }

    private fun snooze(context: Context) {
        API.led(context, 0, 20, 0)
    }

    private fun alarmTriggered(context: Context) {
        API.led(context, 10, 255, 0)
        API.action(context, Actions.nightMode.disable())
    }

    private fun alarmDismissed(context: Context) {
        API.led(context, 10, 255, 0)
        API.action(context, Actions.nightMode.disable())
        setSyncthing(context, false)
    }

    private fun smartPeriod(context: Context) {
        API.led(context, 0, 20, 0)
    }

    private fun setSyncthing(context: Context, enable: Boolean) {
        val intent = Intent()
        val arg = if (enable) "START" else "STOP"
        intent.action = "com.nutomic.syncthingandroid.action.$arg"
        intent.`package` = "com.nutomic.syncthingandroid"
        context.sendBroadcast(intent)
    }

    companion object {

        const val SSID = "Internetjes"

        const val INTENT_START = "com.urbandroid.sleep.alarmclock.START_SLEEP_TRACK"
        const val INTENT_PAUSE = "com.urbandroid.sleep.ACTION_PAUSE_TRACKING"
        const val INTENT_STOP = "com.urbandroid.sleep.alarmclock.STOP_SLEEP_TRACK"
        const val INTENT_SNOOZE = "com.urbandroid.sleep.alarmclock.ALARM_SNOOZE"
        const val INTENT_DISMISS = "com.urbandroid.sleep.alarmclock.ALARM_DISMISS_CAPTCHA"

        const val ACTION_START = "com.urbandroid.sleep.alarmclock.SLEEP_TRACKING_STARTED"
        const val ACTION_STOP = "com.urbandroid.sleep.alarmclock.SLEEP_TRACKING_STOPPED"
        const val ACTION_SNOOZE = "com.urbandroid.sleep.alarmclock.ALARM_SNOOZE_CLICKED_ACTION"
        const val ACTION_ALARM_TRIGGERED = "com.urbandroid.sleep.alarmclock.ALARM_ALERT_START"
        const val ACTION_ALARM_DISMISSED = "com.urbandroid.sleep.alarmclock.ALARM_ALERT_DISMISS"
        const val ACTION_SMART_PERIOD = "com.urbandroid.sleep.alarmclock.AUTO_START_SLEEP_TRACK"

        val filter: IntentFilter
            get() {
                val intentFilter = IntentFilter()
                intentFilter.addAction(ACTION_START)
                intentFilter.addAction(ACTION_STOP)
                intentFilter.addAction(ACTION_SNOOZE)
                intentFilter.addAction(ACTION_ALARM_TRIGGERED)
                intentFilter.addAction(ACTION_ALARM_DISMISSED)
                intentFilter.addAction(ACTION_SMART_PERIOD)
                return intentFilter
            }
    }
}
