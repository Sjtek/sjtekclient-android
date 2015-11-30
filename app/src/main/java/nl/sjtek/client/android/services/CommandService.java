package nl.sjtek.client.android.services;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.update.Action;


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
            execute(Action.Music.TOGGLE.toString());
        } else if (ACTION_MUSIC_NEXT.equals(action)) {
            execute(Action.Music.NEXT.toString());
        } else if (ACTION_SWITCH.equals(action)) {
            execute(Action.SWITCH.toString());
        }
    }

    private void execute(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedInputStream bufIn =
                        new BufferedInputStream(connection.getInputStream());
                byte[] buffer = new byte[1024];
                int n;
                ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
                while ((n = bufIn.read(buffer)) > 0) {
                    bufOut.write(buffer, 0, n);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
