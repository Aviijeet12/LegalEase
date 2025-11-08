package cm.avisingh.legalease.security

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AccessLogWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val securityManager = SecurityManager(context, firestore, auth)

    override suspend fun doWork(): Result {
        val userId = inputData.getString("userId") ?: return Result.failure()
        val action = inputData.getString("action") ?: return Result.failure()
        val documentPath = inputData.getString("documentPath") ?: return Result.failure()
        val timestamp = inputData.getLong("timestamp", System.currentTimeMillis())

        return try {
            val deviceInfo = securityManager.getDeviceInfo()
            
            firestore.collection("access_logs")
                .add(
                    hashMapOf(
                        "userId" to userId,
                        "action" to action,
                        "documentPath" to documentPath,
                        "timestamp" to timestamp,
                        "deviceInfo" to deviceInfo
                    )
                )
                .await()

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}