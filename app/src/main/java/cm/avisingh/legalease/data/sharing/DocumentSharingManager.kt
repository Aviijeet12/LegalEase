package cm.avisingh.legalease.data.sharing

import android.content.Context
import android.net.Uri
import cm.avisingh.legalease.data.model.FirebaseDocument
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class DocumentSharingManager(private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val documentsCollection = firestore.collection("documents")
    private val sharingCollection = firestore.collection("document_shares")

    suspend fun shareDocument(document: FirebaseDocument, userEmails: List<String>): List<String> {
        val successfulShares = mutableListOf<String>()
        
        // Update document sharing settings
        documentsCollection.document(document.id)
            .update("sharedWith", FieldValue.arrayUnion(*userEmails.toTypedArray()))
            .await()

        // Create sharing records
        userEmails.forEach { email ->
            try {
                val shareId = "${document.id}_${email.replace("@", "_at_")}"
                val share = mapOf(
                    "documentId" to document.id,
                    "sharedBy" to document.uploadedBy,
                    "sharedWith" to email,
                    "sharedAt" to FieldValue.serverTimestamp(),
                    "permissions" to listOf("view", "download")
                )
                sharingCollection.document(shareId).set(share).await()
                successfulShares.add(email)
            } catch (e: Exception) {
                // Log failed share
            }
        }

        return successfulShares
    }

    suspend fun createSharingLink(document: FirebaseDocument, expireInDays: Int = 7): Uri {
        // TODO: Re-enable Firebase Dynamic Links when library is added
        // For now, return a simple URL
        return Uri.parse("https://legalease.app/document/${document.id}")
    }

    suspend fun getSharedUsers(documentId: String): List<String> {
        val document = documentsCollection.document(documentId).get().await()
        return document.get("sharedWith") as? List<String> ?: emptyList()
    }

    suspend fun removeSharing(document: FirebaseDocument, userEmail: String) {
        // Remove user from document's sharedWith list
        documentsCollection.document(document.id)
            .update("sharedWith", FieldValue.arrayRemove(userEmail))
            .await()

        // Delete sharing record
        val shareId = "${document.id}_${userEmail.replace("@", "_at_")}"
        sharingCollection.document(shareId).delete().await()
    }

    suspend fun getSharingHistory(documentId: String): List<ShareRecord> {
        return sharingCollection
            .whereEqualTo("documentId", documentId)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                ShareRecord(
                    id = doc.id,
                    documentId = doc.getString("documentId") ?: return@mapNotNull null,
                    sharedBy = doc.getString("sharedBy") ?: return@mapNotNull null,
                    sharedWith = doc.getString("sharedWith") ?: return@mapNotNull null,
                    sharedAt = doc.getTimestamp("sharedAt")?.toDate() ?: return@mapNotNull null,
                    permissions = doc.get("permissions") as? List<String> ?: emptyList()
                )
            }
    }

    suspend fun updateSharingPermissions(
        documentId: String,
        userEmail: String,
        permissions: List<String>
    ) {
        val shareId = "${documentId}_${userEmail.replace("@", "_at_")}"
        sharingCollection.document(shareId)
            .update("permissions", permissions)
            .await()
    }

    suspend fun checkAccess(documentId: String, userEmail: String): ShareAccess {
        val shareId = "${documentId}_${userEmail.replace("@", "_at_")}"
        val share = sharingCollection.document(shareId).get().await()

        return if (!share.exists()) {
            ShareAccess.NO_ACCESS
        } else {
            val permissions = share.get("permissions") as? List<String> ?: emptyList()
            when {
                permissions.contains("edit") -> ShareAccess.EDIT
                permissions.contains("download") -> ShareAccess.DOWNLOAD
                permissions.contains("view") -> ShareAccess.VIEW
                else -> ShareAccess.NO_ACCESS
            }
        }
    }
}

data class ShareRecord(
    val id: String,
    val documentId: String,
    val sharedBy: String,
    val sharedWith: String,
    val sharedAt: Date,
    val permissions: List<String>
)

enum class ShareAccess {
    NO_ACCESS,
    VIEW,
    DOWNLOAD,
    EDIT
}