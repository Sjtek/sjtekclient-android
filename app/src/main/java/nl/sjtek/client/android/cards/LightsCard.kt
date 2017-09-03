package nl.sjtek.client.android.cards

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.card_lights.view.*
import nl.sjtek.client.android.R
import nl.sjtek.client.android.api.API
import nl.sjtek.client.android.storage.StateManager
import nl.sjtek.control.data.actions.Action
import nl.sjtek.control.data.actions.ActionInterface
import nl.sjtek.control.data.responses.ResponseCollection

/**
 * Card for controlling the lights.
 */
class LightsCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCard(context, attrs, defStyleAttr) {

    override fun onShouldInflate(context: Context) {
        inflate(context, R.layout.card_lights, this)
        switch1.setOnClickListener(this::onClick)
        switch2.setOnClickListener(this::onClick)
        switch3.setOnClickListener(this::onClick)
        switch4.setOnClickListener(this::onClick)
        switch5.setOnClickListener(this::onClick)
        switch7.setOnClickListener(this::onClick)

        // Only show the third toggle (LED strip) if the user is allowed to see it.
        if (StateManager.getInstance(getContext()).areExtraLightsEnabled(getContext())) {
            switch3.visibility = View.VISIBLE
            switch4.visibility = View.VISIBLE
        }
    }

    public override fun onUpdate(update: ResponseCollection) {
        val lights = update.lights
        switch1.isChecked = lights.isLight1
        switch2.isChecked = lights.isLight2
        switch3.isChecked = lights.isLight3
        switch4.isChecked = lights.isLight4
        switch5.isChecked = lights.isLight5
        switch7.isChecked = lights.isLight7
    }

    fun onClick(v: View) {
        val enabled = (v as SwitchCompat).isChecked
        when (v.getId()) {
            R.id.switch1 -> toggle(if (enabled) Action.Light.TOGGLE_1_ON else Action.Light.TOGGLE_1_OFF)
            R.id.switch2 -> toggle(if (enabled) Action.Light.TOGGLE_2_ON else Action.Light.TOGGLE_2_OFF)
            R.id.switch3 -> toggle(if (enabled) Action.Light.TOGGLE_3_ON else Action.Light.TOGGLE_3_OFF)
            R.id.switch4 -> toggle(if (enabled) Action.Light.TOGGLE_4_ON else Action.Light.TOGGLE_4_OFF)
            R.id.switch5 -> toggle(if (enabled) Action.Light.TOGGLE_5_ON else Action.Light.TOGGLE_5_OFF)
            R.id.switch7 -> toggle(if (enabled) Action.Light.TOGGLE_7_ON else Action.Light.TOGGLE_7_OFF)
        }
    }

    private fun toggle(action: ActionInterface) {
        API.action(context, action)
    }
}