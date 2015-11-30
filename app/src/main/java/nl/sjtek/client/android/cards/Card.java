package nl.sjtek.client.android.cards;

import android.content.Context;
import android.view.View;

import nl.sjtek.client.android.interfaces.CardCallbackListener;
import nl.sjtek.client.android.interfaces.OnUpdateListener;

/**
 * Created by Wouter Habets on 22-10-15.
 */
public abstract class Card implements OnUpdateListener {

    protected CardCallbackListener cardCallbackListener;

    public Card(CardCallbackListener cardCallbackListener) {
        this.cardCallbackListener = cardCallbackListener;
    }

    public abstract View inflateView(Context context);

    public void setCardCallbackListener(CardCallbackListener listener) {
        cardCallbackListener = listener;
    }
}
