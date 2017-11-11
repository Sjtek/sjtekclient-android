package nl.sjtek.client.android.cards

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import nl.sjtek.client.android.storage.StateManager
import nl.sjtek.control.data.parsers.LampHolder
import nl.sjtek.control.data.parsers.ResponseHolder
import nl.sjtek.control.data.staticdata.Lamp
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Card for creating a UI control for a SjtekControl module.<br></br>
 * This will register on the event bus and subscribe on
 * [ResponseCollection] and [DataCollection] events.
 */
abstract class BaseCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr) {

    init {
        onShouldInflate(context)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val response = StateManager.responseHolder
        if (response != null) onUpdate(response)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        EventBus.getDefault().register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        EventBus.getDefault().unregister(this)
    }

    protected abstract fun onShouldInflate(context: Context)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResponseHolderUpdate(update: ResponseHolder) {
        onUpdate(update)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLampHolderUpdate(update: LampHolder) {
        onLampUpdate(update.lamps)
    }

    internal open fun onUpdate(update: ResponseHolder) {

    }

    internal open fun onLampUpdate(lamps: Map<Int, Lamp>) {

    }
}

