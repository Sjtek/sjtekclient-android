package nl.sjtek.client.android.geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import nl.sjtek.client.android.services.GeofenceIntentService

object GeofenceUtils {

    private const val REQUEST_ID = "sjtek_request"
    private const val LATITUDE = 51.512649
    private const val LONGITUDE = 5.4924752
    private const val RADIUS = 100f

    fun start(context: Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return
        }
        val geofence = Geofence.Builder()
                .setRequestId(REQUEST_ID)
                .setCircularRegion(LATITUDE, LONGITUDE, RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        val request = GeofencingRequest.Builder()
                .addGeofence(geofence)
                .build()
        val intent = Intent(context, GeofenceIntentService::class.java)
        val pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val client = LocationServices.getGeofencingClient(context)
        val task = client.addGeofences(request, pendingIntent)
        task.addOnCompleteListener { task1 ->
            Log.d("GeofenceUtils", "Geofence completed: ${task1.isComplete} result: ${task1.isSuccessful}", task1.exception)
        }
    }
}
