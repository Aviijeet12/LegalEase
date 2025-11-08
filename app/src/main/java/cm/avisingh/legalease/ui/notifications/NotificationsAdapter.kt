package cm.avisingh.legalease.ui.notifications

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ItemNotificationBinding
import cm.avisingh.legalease.notifications.InAppNotification
import cm.avisingh.legalease.notifications.NotificationHelper
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationsAdapter(
    private val onNotificationClick: (InAppNotification) -> Unit,
    private val onNotificationLongClick: (InAppNotification) -> Unit
) : ListAdapter<InAppNotification, NotificationsAdapter.ViewHolder>(NotificationDiffCallback()) {

    private val dateFormat = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())

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

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNotificationClick(getItem(position))
                }
            }

            binding.root.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNotificationLongClick(getItem(position))
                }
                true
            }
        }

        fun bind(notification: InAppNotification) {
            binding.apply {
                // Set icon based on notification type
                val iconResId = when (notification.type) {
                    NotificationHelper.TYPE_DOCUMENT_SHARED -> R.drawable.ic_share
                    NotificationHelper.TYPE_DOCUMENT_UPDATED -> R.drawable.ic_edit
                    NotificationHelper.TYPE_COMMENT_ADDED -> R.drawable.ic_comment
                    NotificationHelper.TYPE_ACCESS_GRANTED -> R.drawable.ic_lock_open
                    NotificationHelper.TYPE_SYSTEM_UPDATE -> R.drawable.ic_system_update
                    else -> R.drawable.ic_notification
                }
                icon.setImageResource(iconResId)

                // Set title and message
                title.text = notification.title
                message.text = notification.message
                timestamp.text = dateFormat.format(notification.timestamp)

                // Apply unread styling
                title.setTypeface(
                    title.typeface,
                    if (notification.isRead) Typeface.NORMAL else Typeface.BOLD
                )
                message.setTypeface(
                    message.typeface,
                    if (notification.isRead) Typeface.NORMAL else Typeface.BOLD
                )
                unreadIndicator.visibility = if (notification.isRead) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                // Set ripple effect color based on read status
                root.setBackgroundResource(
                    if (notification.isRead) {
                        R.drawable.bg_notification_read
                    } else {
                        R.drawable.bg_notification_unread
                    }
                )

                // Animate new notifications
                if (!notification.isRead) {
                    root.alpha = 0f
                    root.translationX = -50f
                    root.animate()
                        .alpha(1f)
                        .translationX(0f)
                        .setDuration(300)
                        .start()
                }
            }
        }
    }
}

private class NotificationDiffCallback : DiffUtil.ItemCallback<InAppNotification>() {
    override fun areItemsTheSame(
        oldItem: InAppNotification,
        newItem: InAppNotification
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: InAppNotification,
        newItem: InAppNotification
    ): Boolean {
        return oldItem == newItem
    }
}