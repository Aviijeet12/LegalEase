package cm.avisingh.legalease.data.repository

import android.content.Context
import android.net.Uri
import cm.avisingh.legalease.data.model.FirebaseDocument
import cm.avisingh.legalease.data.model.Document
import cm.avisingh.legalease.data.storage.DocumentStorage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File

class DocumentRepository(private val context: Context) {
    private val storage = DocumentStorage()
    private val firestore = FirebaseFirestore.getInstance()
    private val documentsCollection = firestore.collection("documents")

    suspend fun uploadDocument(
        userId: String,
        fileUri: Uri,
        fileName: String,
        category: String,
        description: String? = null
    ): FirebaseDocument {
        // Upload to storage
        val metadata = mapOf(
            "uploadedBy" to userId,
            "originalName" to fileName,
            "description" to (description ?: "")
        )
        val storageRef = storage.uploadDocument(userId, fileUri, fileName, category, metadata)
        val downloadUrl = storage.getDocumentUrl(storageRef)

        // Create document object
        val document = FirebaseDocument(
            id = storageRef.name,
            name = fileName,
            url = downloadUrl,
            category = category,
            description = description,
            uploadedBy = userId,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
            size = fileUri.let { uri ->
                context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: 0
            }
        )

        // Save to Firestore
        documentsCollection.document(document.id).set(document).await()

        return document
    }

    suspend fun getDocument(documentId: String): FirebaseDocument? {
        return documentsCollection.document(documentId).get().await()
            .toObject(FirebaseDocument::class.java)
    }

    suspend fun getDocuments(userId: String, category: String? = null): List<FirebaseDocument> {
        var query = documentsCollection.whereEqualTo("uploadedBy", userId)
        if (category != null) {
            query = query.whereEqualTo("category", category)
        }
        return query.get().await().toObjects(FirebaseDocument::class.java)
    }

    suspend fun updateDocument(document: FirebaseDocument) {
        documentsCollection.document(document.id).set(document).await()
    }

    suspend fun deleteDocument(document: FirebaseDocument) {
        // Delete from storage
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(document.url)
        storage.deleteDocument(storageRef)

        // Delete from Firestore
        documentsCollection.document(document.id).delete().await()
    }

    suspend fun downloadDocument(document: FirebaseDocument): File {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(document.url)
        val file = File(context.cacheDir, document.name)
        storage.downloadDocument(storageRef, file)
        return file
    }

    suspend fun getRecentDocuments(userId: String, limit: Int = 10): List<FirebaseDocument> {
        return documentsCollection
            .whereEqualTo("uploadedBy", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(FirebaseDocument::class.java)
    }

    suspend fun searchDocuments(
        userId: String,
        query: String,
        category: String? = null
    ): List<FirebaseDocument> {
        // Basic search implementation - can be enhanced with full-text search later
        val normalizedQuery = query.toLowerCase()
        return getDocuments(userId, category).filter { doc ->
            doc.name.toLowerCase().contains(normalizedQuery) ||
            doc.description?.toLowerCase()?.contains(normalizedQuery) == true
        }
    }

    suspend fun moveDocument(document: FirebaseDocument, newCategory: String) {
        // Create new storage reference
        val oldStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(document.url)
        val userId = document.uploadedBy
        val newStorageRef = storage.documentsRef
            .child(userId)
            .child(newCategory)
            .child(document.name)

        // Download to temp file
        val tempFile = File.createTempFile("move", null, context.cacheDir)
        storage.downloadDocument(oldStorageRef, tempFile)

        // Upload to new location
        val metadata = storage.getMetadata(oldStorageRef)
        val customMeta = mutableMapOf<String, String>()
        // Copy custom metadata
        for (key in metadata.customMetadataKeys) {
            metadata.getCustomMetadata(key)?.let { customMeta[key] = it }
        }
        storage.uploadDocument(
            userId = userId,
            fileUri = Uri.fromFile(tempFile),
            fileName = document.name,
            category = newCategory,
            metadata = customMeta
        )

        // Update document
        val newUrl = storage.getDocumentUrl(newStorageRef)
        val updatedDocument = document.copy(
            category = newCategory,
            url = newUrl,
            updatedAt = Timestamp.now()
        )
        updateDocument(updatedDocument)

        // Delete old file
        storage.deleteDocument(oldStorageRef)
        tempFile.delete()
    }
}