package nl.sjtek.client.android.services;

import android.app.IntentService;
import android.content.Intent;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;


public class CommandService extends IntentService {

    public CommandService() {
        super("CommandService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String ACTION_MUSIC_TOGGLE = getString(R.string.service_action_music_toggle);
        final String ACTION_MUSIC_NEXT = getString(R.string.service_action_music_next);
        final String ACTION_SWITCH = getString(R.string.service_action_switch);

        if (intent == null || intent.getAction() == null) {
            return;
        }

        final String action = intent.getAction();

        if (ACTION_MUSIC_TOGGLE.equals(action)) {
            API.action(getApplicationContext(), Action.Music.TOGGLE);
        } else if (ACTION_MUSIC_NEXT.equals(action)) {
            API.action(getApplicationContext(), Action.Music.NEXT);
        } else if (ACTION_SWITCH.equals(action)) {
            API.action(getApplicationContext(), Action.SWITCH, new Arguments().setUseVoice(true).setDefaultUser(getApplicationContext()));
        }
    }
}
