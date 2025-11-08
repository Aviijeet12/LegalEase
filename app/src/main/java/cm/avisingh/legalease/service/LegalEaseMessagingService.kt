package cm.avisingh.legalease.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import cm.avisingh.legalease.data.repository.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import cm.avisingh.legalease.R
import cm.avisingh.legalease.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LegalEaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Process the notification in repository
        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.processRemoteMessage(remoteMessage)
        }

        // Show system notification if app is in background
        if (shouldShowSystemNotification()) {
            showSystemNotification(remoteMessage)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Update token in repository
        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.updateFcmToken(token)
        }
    }

    private fun shouldShowSystemNotification(): Boolean {
        // Add your logic to determine if system notification should be shown
        // For example, check if app is in background
        return true // For simplicity, always show
    }

    @Inject
    lateinit var notificationChannelManager: NotificationChannelManager

    private fun showSystemNotification(remoteMessage: RemoteMessage) {
        val notificationType = remoteMessage.data["type"] ?: "GENERAL"
        val channelId = notificationChannelManager.getChannelId(notificationType)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent for notification click
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(remoteMessage.data["title"] ?: "New Notification")
            .setContentText(remoteMessage.data["message"] ?: "You have a new notification")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Show notification
        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}