package nl.sjtek.client.android.events;

import nl.sjtek.control.data.settings.DataCollection;

/**
 * Event when authentication was successful.
 */
public class AuthSuccessfulEvent {
    private final DataCollection dataCollection;

    public AuthSuccessfulEvent(DataCollection dataCollection) {
        this.dataCollection = dataCollection;
    }

    public DataCollection getDataCollection() {
        return dataCollection;
    }
}
