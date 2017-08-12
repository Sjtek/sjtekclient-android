package nl.sjtek.client.android.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import nl.sjtek.client.android.api.API
import nl.sjtek.client.android.events.ConnectionEvent
import nl.sjtek.control.data.responses.ResponseCollection
import okhttp3.*
import org.greenrobot.eventbus.EventBus

/**
 * Service for receiving API updates over a websocket.<br></br>
 * Sends the update as a [ResponseCollection] over the bus.<br></br>
 * Sends a [ConnectionEvent] on connection changes.
 */
class UpdateService : Service() {

    private var webSocket: WebSocket? = null

    override fun onCreate() {
        super.onCreate()
        API.info(applicationContext)
        webSocket?.close(1000, null)

        val request = Request.Builder()
                .url("ws://ws.sjtek.nl")
                .build()
        webSocket = OkHttpClient().newWebSocket(request, Client())
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, null)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private inner class Client : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            super.onOpen(webSocket, response)
            EventBus.getDefault().post(ConnectionEvent(true))
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            super.onMessage(webSocket, text)
            val rc = ResponseCollection(text)
            EventBus.getDefault().post(rc)
        }

        override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
            super.onClosed(webSocket, code, reason)
            EventBus.getDefault().post(ConnectionEvent(false))
        }
    }
}
