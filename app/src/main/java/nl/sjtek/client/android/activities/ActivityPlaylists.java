package nl.sjtek.client.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.adapters.PlaylistAdapter;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.client.android.storage.StateManager;

/**
 * Activity for displaying a list of the users playlists. Will be shown as a dialog.
 */
public class ActivityPlaylists extends AppCompatActivity {

    private final Map<String, String> playlists = new HashMap<>();
    private final List<String> playlistNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        playlists.putAll(StateManager.getInstance(this).getDataCollection().getUsers().get(Preferences.getInstance(this).getUsername()).getPlaylists());
        playlistNames.addAll(playlists.keySet());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PlaylistAdapter(playlists, playlistNames));
    }

}
