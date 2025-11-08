package cm.avisingh.legalease.data.storage

import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File

class DocumentStorage {
    private val storage = FirebaseStorage.getInstance()
    private val documentsRef = storage.reference.child("documents")

    suspend fun uploadDocument(
        userId: String,
        fileUri: Uri,
        fileName: String,
        category: String,
        metadata: Map<String, String>
    ): StorageReference {
        // Create storage path: documents/userId/category/fileName
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString())
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
        val safeName = fileName.replace("[^a-zA-Z0-9.-]".toRegex(), "_")
        val fileRef = documentsRef
            .child(userId)
            .child(category)
            .child(safeName)

        // Create metadata
        val storageMetadata = StorageMetadata.Builder()
            .setContentType(mimeType)
            .apply {
                metadata.forEach { (key, value) ->
                    setCustomMetadata(key, value)
                }
            }
            .build()

        // Upload file
        fileRef.putFile(fileUri, storageMetadata).await()
        return fileRef
    }

    suspend fun getDocumentUrl(reference: StorageReference): String {
        return reference.downloadUrl.await().toString()
    }

    suspend fun downloadDocument(reference: StorageReference, destinationFile: File) {
        reference.getFile(destinationFile).await()
    }

    suspend fun deleteDocument(reference: StorageReference) {
        reference.delete().await()
    }

    suspend fun listDocuments(userId: String, category: String? = null): List<StorageReference> {
        val userRef = documentsRef.child(userId)
        val listResult = if (category != null) {
            userRef.child(category).listAll().await()
        } else {
            userRef.listAll().await()
        }
        return listResult.items
    }

    suspend fun getMetadata(reference: StorageReference): StorageMetadata {
        return reference.metadata.await()
    }

    suspend fun updateMetadata(
        reference: StorageReference,
        updates: Map<String, String>
    ): StorageMetadata {
        val currentMetadata = reference.metadata.await()
        val newMetadata = StorageMetadata.Builder(currentMetadata)
            .apply {
                updates.forEach { (key, value) ->
                    setCustomMetadata(key, value)
                }
            }
            .build()
        return reference.updateMetadata(newMetadata).await()
    }
}