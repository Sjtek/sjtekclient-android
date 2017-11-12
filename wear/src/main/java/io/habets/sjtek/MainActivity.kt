package io.habets.sjtek

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import nl.sjtek.control.data.staticdata.Lamp
import kotlin.concurrent.thread

class MainActivity : WearableActivity() {

    private val lamps: MutableList<Lamp> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
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
            }
        }
    }
}
