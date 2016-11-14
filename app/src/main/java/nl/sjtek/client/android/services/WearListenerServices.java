package nl.sjtek.client.android.services;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearListenerServices extends WearableListenerService {
    public WearListenerServices() {
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);
        CommandService.sendCustomAction(getApplicationContext(), "switch");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        CommandService.sendCustomAction(getApplicationContext(), "switch");
    }
}
