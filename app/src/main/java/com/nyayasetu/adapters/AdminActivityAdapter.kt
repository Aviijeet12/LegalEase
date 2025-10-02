package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R

data class Activity(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: String = "",
    val type: String = ""
)

class AdminActivityAdapter(
    private var activities: List<Activity>
) : RecyclerView.Adapter<AdminActivityAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_case, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.bind(activity)
    }

    override fun getItemCount() = activities.size

    fun updateData(newActivities: List<Activity>) {
        activities = newActivities
        notifyDataSetChanged()
    }

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.caseTitle)
        private val descriptionText: TextView = itemView.findViewById(R.id.caseStatus)
        private val timestampText: TextView = itemView.findViewById(R.id.lastUpdate)

        fun bind(activity: Activity) {
            titleText.text = activity.title
            descriptionText.text = activity.description
            timestampText.text = activity.timestamp
        }
    }
}