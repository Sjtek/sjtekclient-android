package nl.sjtek.client.android.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nl.sjtek.control.data.responses.ResponseCollection;

public abstract class BaseCard extends CardView {

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    protected abstract void onShouldInflate(Context context);

    @Subscribe(threadMode = ThreadMode.MAIN)
    protected void onUpdate(ResponseCollection update) {

    }
}

