package nl.sjtek.client.android.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import nl.sjtek.client.android.update.Update;

/**
 * Created by Wouter Habets on 24-1-16.
 */
public class CardManager extends LinearLayout {

    private List<BaseCard> cards;

    public CardManager(Context context) {
        this(context, null);
    }

    public CardManager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addCard(BaseCard baseCard) {
        addView(baseCard);
        cards.add(baseCard);
    }

    public void onUpdate(Update update) {
        for (BaseCard card : cards) {
            card.onUpdate(update);
        }
    }
}
