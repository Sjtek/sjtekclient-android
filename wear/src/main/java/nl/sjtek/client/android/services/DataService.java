package nl.sjtek.client.android.services;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import nl.sjtek.client.android.storage.Preferences;

public class DataService extends WearableListenerService {
    public DataService() {
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                Preferences preferences = Preferences.getInstance(getApplicationContext());
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().contains("/weather")) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    int inside = dataMap.getInt("inside");
                    int outside = dataMap.getInt("outside");
                    preferences.setTemperature(inside, outside);
                }
            }
        }
    }
}
