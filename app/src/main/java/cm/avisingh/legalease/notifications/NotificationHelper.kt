package cm.avisingh.legalease.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cm.avisingh.legalease.R
import cm.avisingh.legalease.ui.dashboard.DashboardActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_DOCUMENTS = "documents"
        const val CHANNEL_SHARING = "sharing"
        const val CHANNEL_SYSTEM = "system"
        
        const val TYPE_DOCUMENT_SHARED = "document_shared"
        const val TYPE_DOCUMENT_UPDATED = "document_updated"
        const val TYPE_COMMENT_ADDED = "comment_added"
        const val TYPE_ACCESS_GRANTED = "access_granted"
        const val TYPE_SYSTEM_UPDATE = "system_update"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_DOCUMENTS,
                    "Document Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications about document updates and changes"
                },
                NotificationChannel(
                    CHANNEL_SHARING,
                    "Sharing Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications about document sharing and collaboration"
                },
                NotificationChannel(
                    CHANNEL_SYSTEM,
                    "System Notifications",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "System updates and announcements"
                }
            )

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            channels.forEach { channel ->
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    fun showNotification(
        type: String,
        title: String,
        message: String,
        data: Map<String, String> = emptyMap()
    ) {
        val channelId = when (type) {
            TYPE_DOCUMENT_SHARED, TYPE_DOCUMENT_UPDATED, TYPE_COMMENT_ADDED -> CHANNEL_DOCUMENTS
            TYPE_ACCESS_GRANTED -> CHANNEL_SHARING
            TYPE_SYSTEM_UPDATE -> CHANNEL_SYSTEM
            else -> CHANNEL_SYSTEM
        }

        val intent = when (type) {
            TYPE_DOCUMENT_SHARED, TYPE_DOCUMENT_UPDATED -> {
                data["documentId"]?.let { documentId ->
                    Intent(context, DocumentViewerActivity::class.java).apply {
                        putExtra(DocumentViewerActivity.EXTRA_DOCUMENT_ID, documentId)
                    }
                }
            }
            TYPE_COMMENT_ADDED -> {
                data["documentId"]?.let { documentId ->
                    Intent(context, DocumentCommentsActivity::class.java).apply {
                        putExtra(DocumentCommentsActivity.EXTRA_DOCUMENT_ID, documentId)
                    }
                }
            }
            else -> Intent(context, DashboardActivity::class.java)
        } ?: Intent(context, DashboardActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(
                when (channelId) {
                    CHANNEL_SHARING -> NotificationCompat.PRIORITY_HIGH
                    CHANNEL_DOCUMENTS -> NotificationCompat.PRIORITY_DEFAULT
                    else -> NotificationCompat.PRIORITY_LOW
                }
            )
            .build()

        NotificationManagerCompat.from(context).notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    fun cancelNotification(id: Int) {
        NotificationManagerCompat.from(context).cancel(id)
    }

    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }
}