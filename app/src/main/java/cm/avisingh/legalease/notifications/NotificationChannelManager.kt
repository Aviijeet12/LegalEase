package cm.avisingh.legalease.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import cm.avisingh.legalease.R

class NotificationChannelManager(
    private val context: Context
) {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannelInfo(
                    CHANNEL_ID_GENERAL,
                    R.string.channel_name_general,
                    R.string.channel_description_general,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                NotificationChannelInfo(
                    CHANNEL_ID_CASE_UPDATES,
                    R.string.channel_name_case_updates,
                    R.string.channel_description_case_updates,
                    NotificationManager.IMPORTANCE_HIGH
                ),
                NotificationChannelInfo(
                    CHANNEL_ID_DOCUMENTS,
                    R.string.channel_name_documents,
                    R.string.channel_description_documents,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                NotificationChannelInfo(
                    CHANNEL_ID_APPOINTMENTS,
                    R.string.channel_name_appointments,
                    R.string.channel_description_appointments,
                    NotificationManager.IMPORTANCE_HIGH
                ),
                NotificationChannelInfo(
                    CHANNEL_ID_SECURITY,
                    R.string.channel_name_security,
                    R.string.channel_description_security,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )

            val notificationManager = NotificationManagerCompat.from(context)
            channels.forEach { channelInfo ->
                createNotificationChannel(channelInfo, notificationManager)
            }
        }
    }

    private fun createNotificationChannel(
        channelInfo: NotificationChannelInfo,
        notificationManager: NotificationManagerCompat
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelInfo.id,
                context.getString(channelInfo.nameResId),
                channelInfo.importance
            ).apply {
                description = context.getString(channelInfo.descriptionResId)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getChannelId(notificationType: String): String {
        return when (notificationType) {
            "CASE_UPDATE" -> CHANNEL_ID_CASE_UPDATES
            "DOCUMENT_READY" -> CHANNEL_ID_DOCUMENTS
            "APPOINTMENT" -> CHANNEL_ID_APPOINTMENTS
            "SECURITY" -> CHANNEL_ID_SECURITY
            else -> CHANNEL_ID_GENERAL
        }
    }

    private data class NotificationChannelInfo(
        val id: String,
        val nameResId: Int,
        val descriptionResId: Int,
        val importance: Int
    )

    companion object {
        const val CHANNEL_ID_GENERAL = "general_notifications"
        const val CHANNEL_ID_CASE_UPDATES = "case_updates"
        const val CHANNEL_ID_DOCUMENTS = "document_notifications"
        const val CHANNEL_ID_APPOINTMENTS = "appointment_reminders"
        const val CHANNEL_ID_SECURITY = "security_alerts"
    }
}