package nl.sjtek.client.android.storage;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.settings.DataCollection;

/**
 * Created by wouter on 24-11-16.
 */

public class WearSyncThread extends Thread {
    private final ResponseCollection responseCollection;
    private final DataCollection dataCollection;
    private final Context context;

    public WearSyncThread(Context context, ResponseCollection responseCollection, DataCollection dataCollection) {
        this.context = context.getApplicationContext();
        this.responseCollection = responseCollection;
        this.dataCollection = dataCollection;
    }

    @Override
    public void run() {
        super.run();
        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                apiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.e(getClass().getSimpleName(), "Failed to connect to GoogleApiClient.");
            return;
        }

        sendWeather(apiClient, responseCollection.getTemperature().getInside(), responseCollection.getTemperature().getOutside());
    }

    private void sendWeather(GoogleApiClient apiClient, float inside, float outside) {
        PutDataMapRequest mapRequest = PutDataMapRequest.create("/weather");
        mapRequest.getDataMap().putInt("inside", (int) inside);
        mapRequest.getDataMap().putInt("outside", (int) outside);
        PutDataRequest dataRequest = mapRequest.asPutDataRequest();
        dataRequest.setUrgent();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(apiClient, dataRequest);
    }
}
