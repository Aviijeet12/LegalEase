package cm.avisingh.legalease.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import cm.avisingh.legalease.data.NotificationPreferences
import cm.avisingh.legalease.data.dao.NotificationDao
import cm.avisingh.legalease.security.EncryptionManager
import cm.avisingh.legalease.security.InAppNotification
import cm.avisingh.legalease.security.SecurityManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
    private val encryptionManager: EncryptionManager,
    private val securityManager: SecurityManager,
    private val notificationPreferences: NotificationPreferences,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {
    fun getAllNotifications(): Flow<List<InAppNotification>> {
        return notificationDao.getAllNotifications().map { notifications ->
            notifications.map { notification ->
                if (notification.isEncrypted && notification.encryptedPayload != null) {
                    decryptNotification(notification)
                } else {
                    notification
                }
            }
        }
    }

    fun getUnreadNotifications(): Flow<List<InAppNotification>> {
        return notificationDao.getUnreadNotifications().map { notifications ->
            notifications.map { notification ->
                if (notification.isEncrypted && notification.encryptedPayload != null) {
                    decryptNotification(notification)
                } else {
                    notification
                }
            }
        }
    }

    suspend fun processRemoteMessage(remoteMessage: RemoteMessage) {
        withContext(Dispatchers.IO) {
            val data = remoteMessage.data
            
            if (!securityManager.isTrustedSource(data["source"])) {
                securityManager.logSecurityEvent(
                    "UNTRUSTED_SOURCE",
                    "Rejected notification from untrusted source: ${data["source"]}"
                )
                return@withContext
            }

            val notification = InAppNotification(
                id = data["id"] ?: UUID.randomUUID().toString(),
                title = data["title"] ?: "",
                message = data["message"] ?: "",
                source = data["source"] ?: "firebase_fcm",
                signature = data["signature"],
                createdAt = Date(),
                readAt = null,
                expiresAt = data["expiresAt"]?.let { Date(it.toLong()) },
                priority = data["priority"]?.let { 
                    cm.avisingh.legalease.security.NotificationPriority.valueOf(it)
                } ?: cm.avisingh.legalease.security.NotificationPriority.NORMAL,
                type = data["type"]?.let {
                    cm.avisingh.legalease.security.NotificationType.valueOf(it)
                } ?: cm.avisingh.legalease.security.NotificationType.GENERAL,
                metadata = data.filterKeys { it !in RESERVED_KEYS }
            )

            if (!securityManager.verifyNotificationSignature(notification)) {
                securityManager.logSecurityEvent(
                    "INVALID_SIGNATURE",
                    "Rejected notification with invalid signature: ${notification.id}"
                )
                return@withContext
            }

            val encryptedNotification = encryptNotification(notification)
            notificationDao.insert(encryptedNotification)
            
            // Send email notification if enabled
            if (notificationPreferences.getEmailNotificationsEnabled().first()) {
                sendEmailNotification(notification)
            }

            securityManager.logSecurityEvent(
                "NOTIFICATION_RECEIVED",
                "Successfully processed and stored notification: ${notification.id}"
            )
        }
    }

    suspend fun updateFcmToken(token: String) {
        withContext(Dispatchers.IO) {
            notificationPreferences.updateFcmToken(token)
            
            // Update token in Firestore
            auth.currentUser?.uid?.let { userId ->
                firestore.collection("users")
                    .document(userId)
                    .update("fcmToken", token)
                    .await()
            }
        }
    }

    private suspend fun sendEmailNotification(notification: InAppNotification) {
        val user = auth.currentUser ?: return
        
        // Create email notification document in Firestore
        // This will trigger a Cloud Function to send the actual email
        firestore.collection("email_notifications")
            .add(hashMapOf(
                "userId" to user.uid,
                "email" to user.email,
                "title" to notification.title,
                "message" to notification.message,
                "type" to notification.type.name,
                "timestamp" to Date()
            ))
            .await()
    }

    suspend fun markAsRead(notificationId: String) {
        withContext(Dispatchers.IO) {
            notificationDao.getNotificationById(notificationId)?.let { notification ->
                val updatedNotification = notification.copy(readAt = Date())
                notificationDao.update(updatedNotification)
                
                securityManager.logSecurityEvent(
                    "NOTIFICATION_READ",
                    "Notification marked as read: $notificationId"
                )
            }
        }
    }

    private fun encryptNotification(notification: InAppNotification): InAppNotification {
        val sensitiveData = mapOf(
            "title" to notification.title,
            "message" to notification.message,
            "metadata" to notification.metadata.toString()
        )

        val encryptedData = encryptionManager.encrypt(sensitiveData.toString().toByteArray())
        
        return notification.copy(
            title = "[Encrypted]",
            message = "[Encrypted]",
            metadata = emptyMap(),
            encryptedPayload = encryptedData,
            isEncrypted = true
        )
    }

    private fun decryptNotification(notification: InAppNotification): InAppNotification {
        val decryptedData = notification.encryptedPayload?.let {
            String(encryptionManager.decrypt(it))
        } ?: return notification

        // In a real app, implement proper parsing of the decrypted data
        // This is a simplified version
        return notification.copy(
            title = "[Decrypted] ${notification.title}",
            message = decryptedData,
            isEncrypted = false,
            encryptedPayload = null
        )
    }

    companion object {
        private val RESERVED_KEYS = setOf(
            "id", "title", "message", "source", "signature",
            "expiresAt", "priority", "type"
        )
    }
}