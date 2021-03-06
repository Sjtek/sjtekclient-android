package nl.sjtek.client.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.events.ConnectionEvent;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Service for receiving API updates over a websocket.<br>
 * Sends the update as a {@link ResponseCollection} over the bus.<br>
 * Sends a {@link ConnectionEvent} on connection changes.
 */
public class SjtekService extends Service {

    private Client client;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        API.info(getApplicationContext());
        if (client != null) {
            client.close();
        }

        try {
            client = new Client();
            client.connect();
        } catch (URISyntaxException ignored) {

        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (client != null) client.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Client extends WebSocketClient {

        Client() throws URISyntaxException {
            super(new URI("ws://ws.sjtek.nl"));
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            EventBus.getDefault().post(new ConnectionEvent(true));
        }

        @Override
        public void onMessage(String message) {
            ResponseCollection rc = new ResponseCollection(message);
            EventBus.getDefault().post(rc);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            EventBus.getDefault().post(new ConnectionEvent(false));
        }

        @Override
        public void onError(Exception ex) {

        }
    }
}
