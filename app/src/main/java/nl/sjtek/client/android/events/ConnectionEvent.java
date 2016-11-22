package nl.sjtek.client.android.events;

/**
 * Event for connection changes in {@link nl.sjtek.client.android.services.SjtekService}.
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
