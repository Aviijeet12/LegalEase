package cm.avisingh.legalease.services

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageService {

    private val storage: FirebaseStorage = Firebase.storage

    suspend fun uploadDocument(fileUri: Uri, caseId: String, fileName: String): Result<String> {
        return try {
            // Create unique file name
            val fileExtension = fileName.substringAfterLast(".", "")
            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"

            // Create storage reference
            val storageRef = storage.reference
            val documentsRef = storageRef.child("documents/$caseId/$uniqueFileName")

            // Upload file
            val uploadTask = documentsRef.putFile(fileUri).await()

            // Get download URL
            val downloadUrl = documentsRef.downloadUrl.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDocument(fileUrl: String): Result<Unit> {
        return try {
            val storageRef = storage.getReferenceFromUrl(fileUrl)
            storageRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getDocumentUrl(filePath: String): String {
        return storage.reference.child(filePath).downloadUrl.toString()
    }
}