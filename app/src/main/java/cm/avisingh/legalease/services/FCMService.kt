package cm.avisingh.legalease.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import cm.avisingh.legalease.utils.SharedPrefManager

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // Send token to your server or update user document in Firestore
        updateUserFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle different types of notifications
        when (remoteMessage.data["type"]) {
            "case_update" -> handleCaseUpdate(remoteMessage)
            "new_message" -> handleNewMessage(remoteMessage)
            "hearing_reminder" -> handleHearingReminder(remoteMessage)
            else -> handleGeneralNotification(remoteMessage)
        }
    }

    private fun handleCaseUpdate(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"] ?: "Case Update"
        val body = remoteMessage.data["body"] ?: "Your case has been updated"
        val caseId = remoteMessage.data["caseId"]

        // Show notification using NotificationService
        NotificationService(this).showCaseUpdateNotification(title, body)
    }

    private fun handleNewMessage(remoteMessage: RemoteMessage) {
        val senderName = remoteMessage.data["senderName"] ?: "Someone"
        val message = remoteMessage.data["message"] ?: "New message"

        NotificationService(this).showNewMessageNotification(senderName, message)
    }

    private fun handleHearingReminder(remoteMessage: RemoteMessage) {
        val caseTitle = remoteMessage.data["caseTitle"] ?: "Your case"
        val hearingTime = remoteMessage.data["hearingTime"] ?: "soon"

        NotificationService(this).showHearingReminder(caseTitle, hearingTime)
    }

    private fun handleGeneralNotification(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "LegalEase"
        val body = remoteMessage.notification?.body ?: "New notification"

        NotificationService(this).showCaseUpdateNotification(title, body)
    }

    private fun updateUserFCMToken(token: String) {
        // Update user document in Firestore with new FCM token
        val userId = SharedPrefManager(this).getUserId()
        if (userId.isNotEmpty()) {
            val updates = hashMapOf<String, Any>(
                "fcmToken" to token,
                "fcmTokenUpdatedAt" to com.google.firebase.Timestamp.now()
            )

            // Use FirestoreService to update user document
            // This would be implemented based on your Firestore structure
        }
    }
}

// Add to AndroidManifest.xml inside <application> tag:
