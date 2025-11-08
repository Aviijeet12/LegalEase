package cm.avisingh.legalease.notifications

import cm.avisingh.legalease.data.local.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val database: AppDatabase,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val encryptionManager: EncryptionManager,
    private val securityManager: SecurityManager
) {
    private val notificationDao = database.notificationDao()
    private val notificationScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // Start FCM token refresh monitoring
        monitorFCMToken()
        // Start notification sync service
        startNotificationSync()
    }

    fun getAllNotifications(): Flow<List<InAppNotification>> =
        notificationDao.getAllNotifications()
            .map { notifications ->
                notifications.map { notification ->
                    // Decrypt notification data
                    decryptNotification(notification)
                }
            }
            .flowOn(Dispatchers.IO)

    fun getUnreadNotifications(): Flow<List<InAppNotification>> =
        notificationDao.getUnreadNotifications()
            .map { notifications ->
                notifications.map { notification ->
                    decryptNotification(notification)
                }
            }
            .flowOn(Dispatchers.IO)

    fun getUnreadCount(): Flow<Int> =
        notificationDao.getUnreadCount()

    suspend fun saveNotification(notification: InAppNotification) {
        // Validate notification source
        if (!validateNotificationSource(notification)) {
            throw SecurityException("Invalid notification source")
        }

        // Encrypt notification data before saving
        val encryptedNotification = encryptNotification(notification)
        notificationDao.insertNotification(encryptedNotification)

        // Log notification receipt for security auditing
        logNotificationAccess("save", notification.id)
    }

    suspend fun markAsRead(notificationId: Long) {
        // Verify user has access to this notification
        if (!verifyNotificationAccess(notificationId)) {
            throw SecurityException("Unauthorized notification access")
        }

        notificationDao.markAsRead(notificationId)
        logNotificationAccess("read", notificationId)
    }

    suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
        logNotificationAccess("read_all", 0)
    }

    suspend fun deleteNotification(notificationId: Long) {
        // Verify user has access to this notification
        if (!verifyNotificationAccess(notificationId)) {
            throw SecurityException("Unauthorized notification access")
        }

        notificationDao.deleteNotification(notificationId)
        logNotificationAccess("delete", notificationId)
    }

    suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
        logNotificationAccess("delete_all", 0)
    }

    private fun monitorFCMToken() {
        notificationScope.launch {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateFCMToken(task.result)
                }
            }
        }
    }

    private fun startNotificationSync() {
        notificationScope.launch {
            // Sync notifications periodically with proper security checks
            while (isActive) {
                try {
                    syncNotifications()
                    delay(SYNC_INTERVAL)
                } catch (e: Exception) {
                    // Log error securely
                    securityManager.logSecurityEvent(
                        "notification_sync_error",
                        e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private suspend fun syncNotifications() {
        val userId = auth.currentUser?.uid ?: return
        
        // Get notifications with secure pagination
        val notifications = firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .whereGreaterThan("timestamp", getLastSyncTime())
            .orderBy("timestamp")
            .limit(BATCH_SIZE)
            .get()
            .await()

        notifications.documents.forEach { doc ->
            val notification = doc.toObject(InAppNotification::class.java)
            notification?.let {
                if (validateNotificationSource(it)) {
                    saveNotification(it)
                }
            }
        }

        updateLastSyncTime()
    }

    private suspend fun decryptNotification(notification: InAppNotification): InAppNotification {
        return withContext(Dispatchers.Default) {
            notification.copy(
                title = encryptionManager.decrypt(notification.title),
                message = encryptionManager.decrypt(notification.message),
                data = notification.data.mapValues { encryptionManager.decrypt(it.value) }
            )
        }
    }

    private suspend fun encryptNotification(notification: InAppNotification): InAppNotification {
        return withContext(Dispatchers.Default) {
            notification.copy(
                title = encryptionManager.encrypt(notification.title),
                message = encryptionManager.encrypt(notification.message),
                data = notification.data.mapValues { encryptionManager.encrypt(it.value) }
            )
        }
    }

    private suspend fun validateNotificationSource(notification: InAppNotification): Boolean {
        return withContext(Dispatchers.IO) {
            // Verify notification signature
            val isValidSignature = securityManager.verifyNotificationSignature(notification)
            
            // Check if notification is from a trusted source
            val isTrustedSource = securityManager.isTrustedSource(notification.source)
            
            // Verify notification timestamp is within acceptable range
            val isTimestampValid = notification.timestamp >= System.currentTimeMillis() - MAX_NOTIFICATION_AGE

            isValidSignature && isTrustedSource && isTimestampValid
        }
    }

    private suspend fun verifyNotificationAccess(notificationId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val notification = notificationDao.getNotificationById(notificationId)
            notification?.let {
                // Verify the notification belongs to the current user
                val userId = auth.currentUser?.uid
                it.userId == userId
            } ?: false
        }
    }

    private suspend fun logNotificationAccess(action: String, notificationId: Long) {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("notification_logs")
            .add(
                hashMapOf(
                    "userId" to userId,
                    "notificationId" to notificationId,
                    "action" to action,
                    "timestamp" to FieldValue.serverTimestamp(),
                    "deviceInfo" to securityManager.getDeviceInfo()
                )
            )
    }

    fun updateFCMToken(token: String) {
        auth.currentUser?.uid?.let { userId ->
            notificationScope.launch {
                try {
                    // Encrypt token before storing
                    val encryptedToken = encryptionManager.encrypt(token)
                    
                    firestore.collection("users")
                        .document(userId)
                        .update("fcmToken", encryptedToken)
                        
                    // Log token update for security auditing
                    securityManager.logSecurityEvent(
                        "fcm_token_update",
                        "Token updated successfully"
                    )
                } catch (e: Exception) {
                    securityManager.logSecurityEvent(
                        "fcm_token_update_error",
                        e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    suspend fun updateNotificationPreferences(preferences: NotificationPreferences) {
        auth.currentUser?.uid?.let { userId ->
            try {
                // Encrypt preferences before storing
                val encryptedPrefs = encryptPreferences(preferences)
                
                firestore.collection("users")
                    .document(userId)
                    .update("notificationPreferences", encryptedPrefs)
                    
                // Log preferences update
                securityManager.logSecurityEvent(
                    "notification_preferences_update",
                    "Preferences updated successfully"
                )
            } catch (e: Exception) {
                securityManager.logSecurityEvent(
                    "notification_preferences_update_error",
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    private suspend fun encryptPreferences(preferences: NotificationPreferences): Map<String, String> {
        return withContext(Dispatchers.Default) {
            mapOf(
                "pushEnabled" to encryptionManager.encrypt(preferences.pushEnabled.toString()),
                "emailEnabled" to encryptionManager.encrypt(preferences.emailEnabled.toString()),
                "documentSharing" to encryptionManager.encrypt(preferences.documentSharing.toString()),
                "documentUpdates" to encryptionManager.encrypt(preferences.documentUpdates.toString()),
                "comments" to encryptionManager.encrypt(preferences.comments.toString()),
                "systemUpdates" to encryptionManager.encrypt(preferences.systemUpdates.toString())
            )
        }
    }

    companion object {
        private const val SYNC_INTERVAL = 15 * 60 * 1000L // 15 minutes
        private const val BATCH_SIZE = 100L
        private const val MAX_NOTIFICATION_AGE = 30 * 24 * 60 * 60 * 1000L // 30 days
    }
}

data class NotificationPreferences(
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = true,
    val documentSharing: Boolean = true,
    val documentUpdates: Boolean = true,
    val comments: Boolean = true,
    val systemUpdates: Boolean = true
)