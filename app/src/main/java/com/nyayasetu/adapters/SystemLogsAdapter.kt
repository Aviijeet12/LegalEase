package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R

data class SystemLog(
    val id: String = "",
    val message: String = "",
    val level: String = "",
    val timestamp: String = "",
    val source: String = ""
)

class SystemLogsAdapter(
    private var logs: List<SystemLog>
) : RecyclerView.Adapter<SystemLogsAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        holder.bind(log)
    }

    override fun getItemCount() = logs.size

    fun updateData(newLogs: List<SystemLog>) {
        logs = newLogs
        notifyDataSetChanged()
    }

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.itemTitle)
        private val timestampText: TextView = itemView.findViewById(R.id.itemDescription)

        fun bind(log: SystemLog) {
            messageText.text = log.message
            timestampText.text = "${log.level} - ${log.timestamp}"
        }
    }
}