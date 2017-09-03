package nl.sjtek.client.android.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import nl.sjtek.client.android.storage.Preferences

class GeofenceIntentService : IntentService("GeofenceIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (!Preferences.areNotificationEnabled(applicationContext)) return

        val event = GeofencingEvent.fromIntent(intent)
        if (event.hasError()) {
            Log.e(DEBUG, "Geofence error: " + event.errorCode)
            return
        }

        when (event.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> ContextCompat.startForegroundService(this, Intent(this, SjtekService::class.java))
            Geofence.GEOFENCE_TRANSITION_EXIT -> stopService(Intent(this, SjtekService::class.java))
        }
    }

    companion object {
        private val DEBUG = GeofenceIntentService::class.java.simpleName
    }
}
