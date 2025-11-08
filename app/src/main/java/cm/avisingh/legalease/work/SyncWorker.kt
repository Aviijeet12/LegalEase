package cm.avisingh.legalease.work

import android.content.Context
import androidx.work.*
import cm.avisingh.legalease.data.cache.DocumentCache
import cm.avisingh.legalease.data.cache.SyncStatus
import cm.avisingh.legalease.data.repository.DocumentRepository
import cm.avisingh.legalease.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val documentCache = DocumentCache(context)
    private val documentRepository = DocumentRepository(context)
    private val userRepository = UserRepository()

    override suspend fun doWork(): Result {
        try {
            val userId = userRepository.currentUser?.uid ?: return Result.failure()

            // Get pending changes
            val pendingDocuments = documentCache.observePendingChanges().first()

            // Process each pending change
            pendingDocuments.forEach { document ->
                when (document.syncStatus) {
                    SyncStatus.PENDING_UPLOAD -> {
                        // Upload new document
                        val uploadedDoc = documentRepository.uploadDocument(
                            userId = userId,
                            fileUri = document.localUri,
                            fileName = document.name,
                            category = document.category,
                            description = document.description
                        )
                        documentCache.updateDocument(uploadedDoc, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_UPDATE -> {
                        // Update existing document
                        documentRepository.updateDocument(document)
                        documentCache.updateDocument(document, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_DELETE -> {
                        // Delete document
                        documentRepository.deleteDocument(document)
                        documentCache.deleteDocument(document)
                    }
                    SyncStatus.CONFLICT -> {
                        // Handle conflict - for now, server wins
                        val serverDoc = documentRepository.getDocument(document.id)
                        if (serverDoc != null) {
                            documentCache.updateDocument(serverDoc, SyncStatus.SYNCED)
                        } else {
                            documentCache.deleteDocument(document)
                        }
                    }
                    else -> { /* Already synced */ }
                }
            }

            // Fetch and cache any new documents from server
            val serverDocuments = documentRepository.getDocuments(userId)
            serverDocuments.forEach { doc ->
                documentCache.cacheDocument(doc)
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "DocumentSync"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                15, TimeUnit.MINUTES,
                5, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }

        fun runNow(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueue(syncRequest)
        }
    }
}