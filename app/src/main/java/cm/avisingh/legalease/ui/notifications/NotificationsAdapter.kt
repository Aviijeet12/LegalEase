package cm.avisingh.legalease.ui.notifications

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ItemNotificationBinding
import cm.avisingh.legalease.notifications.NotificationHelper
import cm.avisingh.legalease.security.InAppNotification
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
                    cm.avisingh.legalease.security.NotificationType.DOCUMENT_SHARED -> R.drawable.ic_share
                    cm.avisingh.legalease.security.NotificationType.DOCUMENT_UPDATE -> R.drawable.ic_edit
                    cm.avisingh.legalease.security.NotificationType.COMMENT -> R.drawable.ic_comment
                    cm.avisingh.legalease.security.NotificationType.ACCESS_GRANTED -> R.drawable.ic_lock_open
                    cm.avisingh.legalease.security.NotificationType.SYSTEM -> R.drawable.ic_system_update
                    else -> R.drawable.ic_notification
                }
                notificationIcon.setImageResource(iconResId)

                // Set title and message
                notificationTitle.text = notification.title
                notificationText.text = notification.message
                notificationTime.text = dateFormat.format(notification.createdAt)

                // Check if notification is read (readAt is not null)
                val isRead = notification.readAt != null

                // Apply unread styling
                notificationTitle.setTypeface(
                    notificationTitle.typeface,
                    if (isRead) Typeface.NORMAL else Typeface.BOLD
                )
                notificationText.setTypeface(
                    notificationText.typeface,
                    if (isRead) Typeface.NORMAL else Typeface.BOLD
                )
                // TODO: Add unreadIndicator to layout or remove this code
                // unreadIndicator.visibility = if (isRead) View.GONE else View.VISIBLE

                // Set ripple effect color based on read status
                root.setBackgroundResource(
                    if (isRead) {
                        R.drawable.bg_notification_read
                    } else {
                        R.drawable.bg_notification_unread
                    }
                )

                // Animate new notifications
                if (!isRead) {
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