package nl.sjtek.client.android.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nl.sjtek.client.android.R
import nl.sjtek.control.data.staticdata.Lamp

class LampAdapter(val lamps: List<Lamp>, private val callback: (lamp: Lamp) -> Unit) : RecyclerView.Adapter<LampAdapter.ViewHolder>() {

    private var lampState: Map<Int, Boolean> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lamp, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setLamp(lamps[position])

    override fun getItemCount(): Int {
        return lamps.size
    }

    fun updateState(newState: Map<Int, Boolean>) {
        lampState = newState
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textView: TextView = itemView.findViewById(R.id.textViewName)

        init {
            itemView.setOnClickListener {
                callback(lamps[layoutPosition])
            }
        }

        fun setLamp(lamp: Lamp) {
            textView.text = lamp.visibleName
        }
    }
}