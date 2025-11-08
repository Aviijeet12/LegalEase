package cm.avisingh.legalease.security

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SecurityManager(
    private val context: Context,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun verifyNotificationSignature(notification: InAppNotification): Boolean {
        // In a real app, implement digital signature verification
        // For now, we'll do basic validation
        return notification.signature?.isNotEmpty() == true
    }

    fun isTrustedSource(source: String?): Boolean {
        // Verify the source against a list of trusted sources
        return TRUSTED_SOURCES.contains(source)
    }

    fun getDeviceInfo(): Map<String, String> {
        return mapOf(
            "manufacturer" to Build.MANUFACTURER,
            "model" to Build.MODEL,
            "os_version" to Build.VERSION.SDK_INT.toString(),
            "device_id" to getSecureDeviceId(),
            "app_version" to getAppVersion()
        )
    }

    fun logSecurityEvent(eventType: String, details: String) {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("security_logs")
            .add(
                hashMapOf(
                    "userId" to userId,
                    "eventType" to eventType,
                    "details" to details,
                    "deviceInfo" to getDeviceInfo(),
                    "timestamp" to com.google.firebase.Timestamp.now()
                )
            )
    }

    private fun getSecureDeviceId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    private fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "${packageInfo.versionName} (${packageInfo.versionCode})"
        } catch (e: Exception) {
            "unknown"
        }
    }

    companion object {
        private val TRUSTED_SOURCES = setOf(
            "firebase_fcm",
            "app_local",
            "app_server"
        )
    }
}