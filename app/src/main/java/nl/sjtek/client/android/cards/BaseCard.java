package nl.sjtek.client.android.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nl.sjtek.client.android.storage.StateManager;
import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.settings.DataCollection;

/**
 * Card for creating a UI control for a SjtekControl module.<br>
 * This will register on the event bus and subscribe on
 * {@link ResponseCollection} and {@link DataCollection} events.
 */
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
        StateManager stateManager = StateManager.getInstance(getContext());
        if (stateManager.isReady()) {
            onUpdate(stateManager.getResponseCollection());
            onDataUpdate(stateManager.getDataCollection());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    protected abstract void onShouldInflate(Context context);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public final void onResponseCollectionEvent(ResponseCollection update) {
        onUpdate(update);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public final void onDataCollectionEvent(DataCollection data) {
        onDataUpdate(data);
    }

    void onDataUpdate(DataCollection data) {

    }

    void onUpdate(ResponseCollection update) {

    }
}

