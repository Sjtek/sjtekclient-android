package nl.sjtek.client.android.cards

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import kotlinx.android.synthetic.main.card_lights.view.*
import nl.sjtek.client.android.R
import nl.sjtek.client.android.adapters.LampAdapter
import nl.sjtek.client.android.api.API
import nl.sjtek.client.android.storage.StateManager
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.parsers.ResponseHolder
import nl.sjtek.control.data.staticdata.Lamp

/**
 * Card for controlling the lights.
 */
class LightsCard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseCard(context, attrs, defStyleAttr) {

    private val lamps: MutableList<Lamp> = StateManager.lamps?.values?.toMutableList() ?: mutableListOf()
    private val adapter = LampAdapter(lamps, this::onLampClick)

    override fun onShouldInflate(context: Context) {
        inflate(context, R.layout.card_lights, this)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter
    }

    private fun onLampClick(lamp: Lamp) {
        API.action(context, Actions.lights.toggle(id = lamp.id))
    }

    public override fun onUpdate(update: ResponseHolder) {
        adapter.updateState(update.lights.state)
    }

    override fun onLampUpdate(lamps: Map<Int, Lamp>) {
        val adapter = LampAdapter(lamps.values.toList(), this::onLampClick)
        recyclerView.adapter = adapter
    }
}