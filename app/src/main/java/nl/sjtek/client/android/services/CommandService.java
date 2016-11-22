package nl.sjtek.client.android.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.api.API;
import nl.sjtek.client.android.api.Action;
import nl.sjtek.client.android.api.Arguments;
import nl.sjtek.client.android.api.CustomAction;


/**
 * Intent service for some basic API calls.
 */
public class CommandService extends IntentService {

    public static final String EXTRA_CUSTOM_ACTION = "customAction";

    public CommandService() {
        super("CommandService");
    }

    public static void sendCustomAction(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(context.getString(R.string.service_action_custom));
        intent.putExtra(CommandService.EXTRA_CUSTOM_ACTION, "switch");
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String ACTION_MUSIC_TOGGLE = getString(R.string.service_action_music_toggle);
        final String ACTION_MUSIC_NEXT = getString(R.string.service_action_music_next);
        final String ACTION_SWITCH = getString(R.string.service_action_switch);
        final String ACTION_CUSTOM_ACTION = getString(R.string.service_action_custom);

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
        } else if (ACTION_CUSTOM_ACTION.equals(action)) {
            String customAction = intent.getStringExtra(EXTRA_CUSTOM_ACTION);
            if (TextUtils.isEmpty(customAction)) return;
            API.action(getApplicationContext(), new CustomAction(customAction));
        }
    }
}
