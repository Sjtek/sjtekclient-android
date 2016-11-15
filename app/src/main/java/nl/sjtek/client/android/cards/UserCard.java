package nl.sjtek.client.android.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import nl.sjtek.client.android.R;
import nl.sjtek.client.android.storage.Preferences;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 20-3-16.
 */
public class UserCard extends BaseCard {

    public UserCard(Context context) {
        super(context);
    }

    public UserCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_user, this);
        TextView textView = (TextView) findViewById(R.id.textView);
        String username = Preferences.getInstance(getContext()).getUsername().toLowerCase();
        if (username.isEmpty()) {
            textView.setText("Niet ingelogd.");
        } else {
            textView.setText(String.format("Hallo %s%s.", username.substring(0, 1).toUpperCase(), username.substring(1)));
        }
    }

    @Override
    public void onUpdate(ResponseCollection update) {

    }
}
