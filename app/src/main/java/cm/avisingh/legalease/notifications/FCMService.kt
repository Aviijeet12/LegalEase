package cm.avisingh.legalease.notifications

import cm.avisingh.legalease.data.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    // TODO: Initialize these properly after removing Hilt completely
    private val notificationHelper by lazy { NotificationHelper(applicationContext) }
    private val notificationRepository by lazy { 
        // NotificationRepository(...)  // Needs DAO, managers, etc. - stub for now
        null
    }

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
        // TODO: Update FCM token in Firebase after proper DI setup
        // notificationRepository?.updateFCMToken(token)
    }

    private fun saveNotification(
        type: String,
        title: String,
        message: String,
        data: Map<String, String>
    ) {
        // TODO: Save notification after proper DI setup
        // val notification = InAppNotification(...)
        // notificationRepository?.saveNotification(notification)
    }
}