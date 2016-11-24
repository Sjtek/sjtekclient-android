package nl.sjtek.client.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;

/**
 * Adapter for displaying the playlists.<br>
 * Playlists will be shown as simple list items. And will be started on click.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private final Map<String, String> playlists;
    private final List<String> playlistNames;

    public PlaylistAdapter(Map<String, String> playlists, List<String> playlistNames) {
        this.playlists = playlists;
        this.playlistNames = playlistNames;
    }

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
            API.action(view.getContext(), Action.Music.START, new Arguments().setUrl(playlist));
        }
    }
}
