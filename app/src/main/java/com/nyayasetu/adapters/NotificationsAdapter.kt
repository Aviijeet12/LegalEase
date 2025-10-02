package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: String = "general"
)

class NotificationsAdapter(
    private var notifications: List<Notification>,
    private val onNotificationClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        private val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        private val viewUnreadIndicator: View = itemView.findViewById(R.id.viewUnreadIndicator)

        fun bind(notification: Notification) {
            textViewTitle.text = notification.title
            textViewMessage.text = notification.message
            
            val timeAgo = getTimeAgo(notification.timestamp)
            textViewTime.text = timeAgo
            
            viewUnreadIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE
            
            itemView.alpha = if (notification.isRead) 0.7f else 1.0f
            
            itemView.setOnClickListener { onNotificationClick(notification) }
        }
        
        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            val minutes = diff / (1000 * 60)
            val hours = minutes / 60
            val days = hours / 24
            
            return when {
                minutes < 1 -> "Just now"
                minutes < 60 -> "${minutes}m ago"
                hours < 24 -> "${hours}h ago"
                days < 7 -> "${days}d ago"
                else -> "${days / 7}w ago"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size

    fun updateNotifications(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }
    
    fun markAsRead(notificationId: String) {
        val index = notifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            notifications = notifications.toMutableList().apply {
                set(index, get(index).copy(isRead = true))
            }
            notifyItemChanged(index)
        }
    }
}