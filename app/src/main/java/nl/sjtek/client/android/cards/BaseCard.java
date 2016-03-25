package nl.sjtek.client.android.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import nl.sjtek.client.android.api.ActionInterface;
import nl.sjtek.control.data.responses.ResponseCollection;

/**
 * Created by Wouter Habets on 24-1-16.
 */
public abstract class BaseCard extends CardView {

    private OnClickListener onClickListener;

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

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    protected void postAction(ActionInterface action) {
        if (onClickListener != null) {
            onClickListener.onClick(action);
        }
    }

    protected abstract void onShouldInflate(Context context);

    public abstract void onUpdate(ResponseCollection update);

    public interface OnClickListener {
        void onClick(ActionInterface action);
    }
}
