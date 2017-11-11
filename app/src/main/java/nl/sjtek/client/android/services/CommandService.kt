package nl.sjtek.client.android.services

import android.app.IntentService
import android.content.Intent
import nl.sjtek.client.android.R
import nl.sjtek.client.android.api.API
import nl.sjtek.control.data.actions.Actions


/**
 * Intent service for some basic API calls.
 */
class CommandService : IntentService("CommandService") {

    override fun onHandleIntent(intent: Intent?) {
        val ACTION_MUSIC_TOGGLE = getString(R.string.service_action_music_toggle)
        val ACTION_MUSIC_NEXT = getString(R.string.service_action_music_next)
        val ACTION_SWITCH = getString(R.string.service_action_switch)

        if (intent == null || intent.action == null) {
            return
        }

        val action = intent.action

        if (ACTION_MUSIC_TOGGLE == action) {
            API.action(applicationContext, Actions.music.toggle())
        } else if (ACTION_MUSIC_NEXT == action) {
            API.action(applicationContext, Actions.music.next())
        } else if (ACTION_SWITCH == action) {
            API.action(applicationContext, Actions.toggle())
        }
    }
}
