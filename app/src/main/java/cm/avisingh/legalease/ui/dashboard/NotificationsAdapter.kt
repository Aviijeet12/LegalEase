package cm.avisingh.legalease.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.notifications.NotificationHelper
import cm.avisingh.legalease.security.InAppNotification
import cm.avisingh.legalease.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(
    private val onNotificationClick: (InAppNotification) -> Unit,
    private val onMoreClick: (InAppNotification) -> Unit
) : ListAdapter<InAppNotification, NotificationsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: InAppNotification) {
            binding.apply {
                notificationTitle.text = notification.title
                notificationText.text = notification.message
                notificationTime.text = formatTime(notification.createdAt)
                notificationIcon.setImageResource(getNotificationIcon(notification.type))

                root.setOnClickListener { onNotificationClick(notification) }
                moreButton.setOnClickListener { onMoreClick(notification) }
            }
        }

        private fun formatTime(date: Date): String {
            val now = System.currentTimeMillis()
            val diff = now - date.time

            return when {
                diff < 60 * 1000 -> // Less than 1 minute
                    "Just now"
                diff < 60 * 60 * 1000 -> // Less than 1 hour
                    "${diff / (60 * 1000)}m ago"
                diff < 24 * 60 * 60 * 1000 -> // Less than 24 hours
                    "${diff / (60 * 60 * 1000)}h ago"
                diff < 7 * 24 * 60 * 60 * 1000L -> // Less than 7 days
                    SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
                else ->
                    SimpleDateFormat("MMM d", Locale.getDefault()).format(date)
            }
        }

        private fun getNotificationIcon(type: cm.avisingh.legalease.security.NotificationType): Int {
            return when (type) {
                cm.avisingh.legalease.security.NotificationType.DOCUMENT_SHARED -> R.drawable.ic_share
                cm.avisingh.legalease.security.NotificationType.COMMENT -> R.drawable.ic_comment
                cm.avisingh.legalease.security.NotificationType.ACCESS_GRANTED -> R.drawable.ic_lock_open
                cm.avisingh.legalease.security.NotificationType.DOCUMENT_UPDATE -> R.drawable.ic_edit
                cm.avisingh.legalease.security.NotificationType.SYSTEM -> R.drawable.ic_system_update
                else -> R.drawable.ic_notification
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<InAppNotification>() {
            override fun areItemsTheSame(oldItem: InAppNotification, newItem: InAppNotification) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: InAppNotification, newItem: InAppNotification) =
                oldItem == newItem
        }
    }
}