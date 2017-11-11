package nl.sjtek.client.android.cards

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import butterknife.OnClick
import kotlinx.android.synthetic.main.card_coffee.view.*
import nl.sjtek.client.android.R
import nl.sjtek.client.android.api.API
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.parsers.ResponseHolder

/**
 * Card for controlling the coffee machine.
 */
class CoffeeCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCard(context, attrs, defStyleAttr) {

    override fun onShouldInflate(context: Context) {
        inflate(context, R.layout.card_coffee, this)
    }

    @OnClick(R.id.buttonStart)
    fun onStartClick() {
        API.action(context, Actions.coffee.enable())
    }

    override fun onUpdate(update: ResponseHolder) {
        updateViews(update.coffee.enabled)
    }

    private fun updateViews(heated: Boolean) {
        val color = ContextCompat.getColor(context, if (heated)
            R.color.coffee_heated
        else
            R.color.coffee_cold)
        textViewStatus.setTextColor(color)
        textViewStatus.text = if (heated) context.getString(R.string.card_coffee_heated_true) else context.getString(R.string.card_coffee_heated_false)
        imageViewCup.setColorFilter(color)
    }
}
