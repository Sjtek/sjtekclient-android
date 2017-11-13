package io.habets.sjtek

import android.os.Bundle
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_lamps.*
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.staticdata.Lamp
import kotlin.concurrent.thread

class LampsActivity : WearableActivity() {

    private val lamps: MutableList<Lamp> = mutableListOf()
    private val adapter: LampAdapter = LampAdapter(lamps, this::onLampToggle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lamps)

        recyclerView.setHasFixedSize(true)
        recyclerView.isEdgeItemsCenteringEnabled = true
        recyclerView.layoutManager = WearableLinearLayoutManager(this)
        recyclerView.adapter = adapter
        refreshLamps()
    }

    private fun refreshLamps() {
        thread(start = true) {
            var lamps = Storage.getLamps(this)
            if (lamps.isEmpty()) {
                lamps = Net.getLamps(this)
            }
            if (!lamps.isEmpty()) {
                Storage.setLamps(this, lamps)
                this.lamps.clear()
                this.lamps.addAll(lamps)
                runOnUiThread {
                    this.adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun onLampToggle(lamp: Lamp) {
        Net.send(Actions.lights.toggle(id = lamp.id))
    }
}
