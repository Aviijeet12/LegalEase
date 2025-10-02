package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R

data class TimelineEvent(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: String = "",
    val type: String = ""
)

class CaseTimelineAdapter(
    private var events: List<TimelineEvent>
) : RecyclerView.Adapter<CaseTimelineAdapter.TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount() = events.size

    fun updateData(newEvents: List<TimelineEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }

    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.itemTitle)
        private val descriptionText: TextView = itemView.findViewById(R.id.itemDescription)

        fun bind(event: TimelineEvent) {
            titleText.text = event.title
            descriptionText.text = event.description
        }
    }
}