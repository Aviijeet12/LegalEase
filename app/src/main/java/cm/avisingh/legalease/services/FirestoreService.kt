package cm.avisingh.legalease.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        const val COLLECTION_USERS = "users"
        const val COLLECTION_CASES = "cases"
        const val COLLECTION_MESSAGES = "messages"
        const val COLLECTION_DOCUMENTS = "documents"
        const val COLLECTION_NOTIFICATIONS = "notifications"
    }

    // User Operations
    suspend fun addUserData(userId: String, userData: HashMap<String, Any>) {
        try {
            db.collection(COLLECTION_USERS).document(userId).set(userData).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUserData(userId: String): Map<String, Any>? {
        return try {
            val document = db.collection(COLLECTION_USERS).document(userId).get().await()
            if (document.exists()) document.data else null
        } catch (e: Exception) {
            null
        }
    }

    // Case Operations
    suspend fun createCase(caseData: HashMap<String, Any>): String {
        return try {
            val document = db.collection(COLLECTION_CASES).add(caseData).await()
            document.id
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCasesForUser(userId: String, userRole: String): List<Map<String, Any>> {
        return try {
            val query = if (userRole == "lawyer") {
                db.collection(COLLECTION_CASES).whereEqualTo("lawyerId", userId)
            } else {
                db.collection(COLLECTION_CASES).whereEqualTo("clientId", userId)
            }

            val querySnapshot = query.get().await()
            querySnapshot.documents.map { it.data ?: emptyMap() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateCase(caseId: String, updates: Map<String, Any>) {
        try {
            db.collection(COLLECTION_CASES).document(caseId).update(updates).await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Message Operations
    suspend fun sendMessage(chatId: String, messageData: HashMap<String, Any>) {
        try {
            db.collection(COLLECTION_MESSAGES)
                .document(chatId)
                .collection("messages")
                .add(messageData)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    fun getMessagesRealTime(chatId: String, onUpdate: (List<Map<String, Any>>) -> Unit) {
        db.collection(COLLECTION_MESSAGES)
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val messages = snapshot?.documents?.map { doc ->
                    doc.data?.toMutableMap()?.apply {
                        put("id", doc.id)
                    } ?: emptyMap()
                } ?: emptyList()

                onUpdate(messages)
            }
    }

    // Document Operations
    suspend fun addDocumentMetadata(documentData: HashMap<String, Any>): String {
        return try {
            val document = db.collection(COLLECTION_DOCUMENTS).add(documentData).await()
            document.id
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCaseDocuments(caseId: String): List<Map<String, Any>> {
        return try {
            val querySnapshot = db.collection(COLLECTION_DOCUMENTS)
                .whereEqualTo("caseId", caseId)
                .get()
                .await()

            querySnapshot.documents.map { it.data ?: emptyMap() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}