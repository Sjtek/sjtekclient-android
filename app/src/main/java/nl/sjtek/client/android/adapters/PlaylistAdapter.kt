package nl.sjtek.client.android.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nl.sjtek.client.android.R
import nl.sjtek.client.android.api.API
import nl.sjtek.control.data.actions.Actions

/**
 * Adapter for displaying the playlists.<br></br>
 * Playlists will be shown as simple list items. And will be started on click.
 */
class PlaylistAdapter(private val playlists: Map<String, String>, private val playlistNames: List<String>) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setText(playlistNames[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun setText(text: String) {
            (itemView as TextView).text = text
        }

        override fun onClick(view: View) {
            val playlist = playlists[playlistNames[layoutPosition]] ?: "'"
            API.action(view.context, Actions.music.start(
                    uri = playlist,
                    shuffle = true,
                    clear = true,
                    resetVolume = true))
        }

        override fun onLongClick(view: View): Boolean {
            val playlist = playlists[playlistNames[layoutPosition]] ?: ""
            API.action(view.context, Actions.music.start(
                    uri = playlist,
                    shuffle = false,
                    clear = true,
                    resetVolume = true))
            return true
        }
    }
}
