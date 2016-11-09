package nl.sjtek.client.android.events;

import nl.sjtek.control.data.settings.DataCollection;

/**
 * Created by wouter on 9-11-16.
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
