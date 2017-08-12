package nl.sjtek.client.android.events;

import nl.sjtek.client.android.services.UpdateService;

/**
 * Event for connection changes in {@link UpdateService}.
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
