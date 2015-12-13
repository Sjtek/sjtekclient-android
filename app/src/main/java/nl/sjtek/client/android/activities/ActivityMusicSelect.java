package nl.sjtek.client.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import nl.sjtek.client.android.R;

public class ActivityMusicSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.Holder> {

        private Map<String, String> playlists = new HashMap<>();

        public PlaylistAdapter() {
            initList();
        }

        private void initList() {
            playlists.put("SjtekSjpeellijst", "music/start");
            playlists.put("3FM", "music/start?url=http://icecast.omroep.nl/3fm-bb-mp3");
            playlists.put("Tijn: Swek muziek", "music/start?url=spotify:user:1123840057:playlist:1kbSO9MqJMWOdsIfPhjcvW");
            playlists.put("Tijn: Coding rave", "music/start?url=spotify:user:1123840057:playlist:78nuAkhrGgF7rYbVBSj2t5");
            playlists.put("Wouter: Dinges 4", "music/start?url=spotify:user:1133212423:playlist:6GoCxjOJ5pXgr74Za0z9bt");
            playlists.put("Wouter: Dinges 3", "music/start?url=spotify:user:1133212423:playlist:6clH0v0FfHyjsEiHgULafH");
            playlists.put("Kevin: Sjpeellijst", "music/start?url=spotify:user:1130395265:playlist:5UOGVcoR34i1XUFLYCXbnz");
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_playlist, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return playlists.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            private TextView textViewTitle;

            public Holder(View itemView) {
                super(itemView);
                textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            }

            public void setTitle(String text) {
                textViewTitle.setText(text);
            }
        }
    }
}
