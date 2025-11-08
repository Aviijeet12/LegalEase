package cm.avisingh.legalease.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var notificationRepository: NotificationRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle notification when app is in foreground
        remoteMessage.notification?.let { notification ->
            val type = remoteMessage.data["type"] ?: NotificationHelper.TYPE_SYSTEM_UPDATE
            
            // Show notification
            notificationHelper.showNotification(
                type = type,
                title = notification.title ?: "",
                message = notification.body ?: "",
                data = remoteMessage.data
            )

            // Save notification to local database
            saveNotification(
                type = type,
                title = notification.title ?: "",
                message = notification.body ?: "",
                data = remoteMessage.data
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Update FCM token in Firebase
        notificationRepository.updateFCMToken(token)
    }

    private fun saveNotification(
        type: String,
        title: String,
        message: String,
        data: Map<String, String>
    ) {
        val notification = InAppNotification(
            type = type,
            title = title,
            message = message,
            data = data,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        notificationRepository.saveNotification(notification)
    }
}