package io.habets.sjtek

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nl.sjtek.control.data.staticdata.Lamp

class LampAdapter(val lamps: MutableList<Lamp>, private val clickListener: (lamp: Lamp) -> Unit) : RecyclerView.Adapter<LampAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lamp = lamps[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lamp, parent, false))
    }

    override fun getItemCount(): Int = lamps.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView as TextView

        init {
            itemView.setOnClickListener { clickListener(this.lamp ?: return@setOnClickListener) }
        }

        var lamp: Lamp? = null
            set(value) {
                field = value
                textView.text = value?.visibleName
            }
    }
}
