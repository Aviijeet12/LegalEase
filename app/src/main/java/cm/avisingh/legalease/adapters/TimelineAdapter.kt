package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.models.TimelineEvent

class TimelineAdapter : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    private val events: MutableList<TimelineEvent> = mutableListOf()

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEventTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        private val tvEventDescription: TextView = itemView.findViewById(R.id.tvEventDescription)
        private val tvEventDate: TextView = itemView.findViewById(R.id.tvEventDate)
        private val tvEventType: TextView = itemView.findViewById(R.id.tvEventType)

        fun bind(event: TimelineEvent) {
            tvEventTitle.text = event.title
            tvEventDescription.text = event.description
            tvEventDate.text = event.getFormattedDate()
            tvEventType.text = event.eventType

            // Set event type background color
            tvEventType.setBackgroundColor(
                ContextCompat.getColor(itemView.context, event.getEventTypeColor())
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline_event, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<TimelineEvent>) {
        val oldSize = events.size
        events.clear()
        events.addAll(newEvents)

        // More efficient than notifyDataSetChanged()
        if (oldSize == 0) {
            notifyItemRangeInserted(0, newEvents.size)
        } else {
            notifyItemRangeChanged(0, oldSize)
            if (newEvents.size > oldSize) {
                notifyItemRangeInserted(oldSize, newEvents.size - oldSize)
            } else if (newEvents.size < oldSize) {
                notifyItemRangeRemoved(newEvents.size, oldSize - newEvents.size)
            }
        }
    }
}