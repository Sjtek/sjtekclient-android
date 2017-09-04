package nl.sjtek.client.android.cards

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.card_lights.view.*
import nl.sjtek.client.android.R
import nl.sjtek.client.android.api.API
import nl.sjtek.client.android.storage.StateManager
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.parsers.ResponseHolder

/**
 * Card for controlling the lights.
 */
class LightsCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCard(context, attrs, defStyleAttr) {

    override fun onShouldInflate(context: Context) {
        inflate(context, R.layout.card_lights, this)
        switch1.setOnClickListener(this::onClick)
        switch3.setOnClickListener(this::onClick)
        switch4.setOnClickListener(this::onClick)
        switch6.setOnClickListener(this::onClick)
        switch8.setOnClickListener(this::onClick)
        switch7.setOnClickListener(this::onClick)

        // Only show the third toggle (LED strip) if the user is allowed to see it.
        if (StateManager.areExtraLightsEnabled(context)) {
            switch3.visibility = View.VISIBLE
            switch4.visibility = View.VISIBLE
        }
    }

    public override fun onUpdate(update: ResponseHolder) {
        val lights = update.lights.state
        switch1.isChecked = lights[1] == true
        switch3.isChecked = lights[3] == true
        switch4.isChecked = lights[4] == true
        switch6.isChecked = lights[6] == true
        switch8.isChecked = lights[8] == true
        switch7.isChecked = lights[7] == true
    }

    fun onClick(v: View) {
        val enabled = (v as SwitchCompat).isChecked
        when (v.getId()) {
            R.id.switch1 -> toggle(if (enabled) enable(1) else disable(1))
            R.id.switch3 -> toggle(if (enabled) enable(3) else disable(3))
            R.id.switch4 -> toggle(if (enabled) enable(4) else disable(4))
            R.id.switch6 -> toggle(if (enabled) enable(6) else disable(6))
            R.id.switch8 -> toggle(if (enabled) enable(8) else disable(8))
            R.id.switch7 -> toggle(if (enabled) enable(1) else disable(1))
        }
    }

    private fun enable(id: Int): String = Actions.lights.toggle(id, enabled = true)
    private fun disable(id: Int): String = Actions.lights.toggle(id, enabled = false)

    private fun toggle(action: String) {
        API.action(context, action)
    }
}