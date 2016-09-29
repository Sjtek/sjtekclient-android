package nl.sjtek.client.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import nl.sjtek.client.android.events.ConnectionEvent;
import nl.sjtek.control.data.responses.ResponseCollection;

public class SjtekService extends Service {

    private Client client = new Client();

    public SjtekService() throws URISyntaxException {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        client.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.close();
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
