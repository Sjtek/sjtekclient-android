package nl.sjtek.client.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;
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
        recyclerView.setAdapter(new PlaylistAdapter());
    }

    /**
     * Adapter for displaying the playlists.<br>
     * Playlists will be shown as simple list items. And will be started on click.
     */
    private class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setText(playlistNames.get(position));
        }

        @Override
        public int getItemCount() {
            return playlists.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
            }

            public void setText(String text) {
                ((TextView) itemView).setText(text);
            }

            @Override
            public void onClick(View view) {
                String playlist = playlists.get(playlistNames.get(getLayoutPosition()));
                API.action(getApplicationContext(), Action.Music.START, new Arguments().setUrl(playlist));
                finish();
            }
        }
    }
}
