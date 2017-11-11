package nl.sjtek.client.android.fragments

import android.os.Bundle
import android.view.*
import nl.sjtek.client.android.R
import nl.sjtek.client.android.api.API
import nl.sjtek.control.data.actions.Actions

/**
 * Fragment for the Mopidy web interface.
 * Displays extra toolbar action buttons for controlling Mopidy.
 */
class FragmentMusic : BaseFragmentWeb() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getUrl(): String {
        return "http://music.sjtek.nl/musicbox_webclient/index.html#home"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_music, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_shuffle -> {
                API.action(context, Actions.music.shuffle())
                true
            }
            R.id.action_clear -> {
                API.action(context, Actions.music.clear())
                true
            }
            R.id.action_start -> {
                //TODO Add URI
                API.action(context, Actions.music.start(uri = ""))
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
