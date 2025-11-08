package cm.avisingh.legalease.security

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class AccessLogWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val firestore: FirebaseFirestore,
    private val securityManager: SecurityManager
) : CoroutineWorker(context, params) {

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