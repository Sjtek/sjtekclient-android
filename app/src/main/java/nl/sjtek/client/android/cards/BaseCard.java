package nl.sjtek.client.android.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import nl.sjtek.client.android.update.Update;

/**
 * Created by Wouter Habets on 24-1-16.
 */
public abstract class BaseCard extends RelativeLayout {

    public BaseCard(Context context) {
        this(context, null);
    }

    public BaseCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onShouldInflate(context);
    }

    protected abstract void onShouldInflate(Context context);

    public abstract void onUpdate(Update update);
}
