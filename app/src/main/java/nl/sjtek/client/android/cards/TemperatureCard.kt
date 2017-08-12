package nl.sjtek.client.android.cards

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.card_temperature.view.*
import nl.sjtek.client.android.R
import nl.sjtek.control.data.responses.ResponseCollection

/**
 * Card for displaying the temperature (inside and outside).
 */
class TemperatureCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCard(context, attrs, defStyleAttr) {

    override fun onShouldInflate(context: Context) {
        inflate(context, R.layout.card_temperature, this)
    }

    public override fun onUpdate(update: ResponseCollection) {
        val temperature = update.temperature
        textViewTempInside.text = temperature.inside.toInt().toString()
        textViewTempOutside.text = temperature.outside.toInt().toString()
    }
}
