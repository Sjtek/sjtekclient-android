package nl.sjtek.client.android.cards;

import android.content.Context;
import android.util.AttributeSet;

import nl.sjtek.client.android.R;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by wouter on 16-1-17.
 */

public class NightModeCard extends BaseCard {
    public NightModeCard(Context context) {
        super(context);
    }

    public NightModeCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NightModeCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onShouldInflate(Context context) {
        inflate(context, R.layout.card_night_mode, this);
    }

    @Override
    void onUpdate(ResponseCollection update) {
        super.onUpdate(update);
        setVisibility(update.getNightMode().isEnabled() ? VISIBLE : GONE);
    }
}
