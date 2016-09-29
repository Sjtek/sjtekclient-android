package nl.sjtek.client.android.events;

/**
 * Created by wouter on 29-9-16.
 */

public class ConnectionEvent {
    private final boolean connected;

    public ConnectionEvent(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
