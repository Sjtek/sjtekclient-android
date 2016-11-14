package nl.sjtek.client.android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.ActionSender;
import nl.sjtek.client.android.fragments.FragmentGrid;
import nl.sjtek.client.android.fragments.LightsFragment;
import nl.sjtek.client.android.fragments.MusicFragment;
import nl.sjtek.client.android.fragments.PlaylistFragment;
import nl.sjtek.client.android.fragments.WeatherFragment;

public class MainActivity extends WearableActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener,
        ActionSender {

    private FragmentGrid grid;

    private GridViewPager viewPager;
    private CardAdapter adapter;
    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = new FragmentGrid(
                new FragmentGrid.Row(new LightsFragment()),
                new FragmentGrid.Row(new MusicFragment(), new PlaylistFragment()),
                new FragmentGrid.Row(new WeatherFragment())
        );

        adapter = new CardAdapter(getFragmentManager());

        viewPager = (GridViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(apiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (apiClient != null && apiClient.isConnected()) {
            apiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(apiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            switch (event.getType()) {
                case DataEvent.TYPE_CHANGED:
                    break;
                case DataEvent.TYPE_DELETED:
                    break;
            }
        }
    }

    @Override
    public void sendAction(String action) {
        if (apiClient.isConnected()) {
            PutDataMapRequest mapRequest = PutDataMapRequest.create("/action");
            mapRequest.getDataMap().putString("action", action);
            PutDataRequest dataRequest = mapRequest.asPutDataRequest();
            dataRequest.setUrgent();
            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(apiClient, dataRequest);
        }
    }

    private class CardAdapter extends FragmentGridPagerAdapter {

        CardAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getRowCount() {
            return grid.getRowsCount();
        }

        @Override
        public int getColumnCount(int row) {
            return grid.getColumnCount(row);
        }

        @Override
        public Fragment getFragment(int row, int column) {
            return grid.get(row, column);
        }
    }
}
