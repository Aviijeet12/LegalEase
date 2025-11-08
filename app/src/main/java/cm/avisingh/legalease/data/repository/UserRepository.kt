package cm.avisingh.legalease.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import cm.avisingh.legalease.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun createUser(user: User) {
        usersCollection.document(user.id).set(user).await()
    }

    suspend fun getUser(userId: String): User? {
        return usersCollection.document(userId).get().await().toObject(User::class.java)
    }

    suspend fun updateUser(user: User) {
        usersCollection.document(user.id).set(user).await()
    }

    suspend fun updateLastLogin(userId: String) {
        usersCollection.document(userId).update("lastLoginAt", System.currentTimeMillis()).await()
    }

    fun signOut() {
        auth.signOut()
    }

    fun deleteAccount() {
        currentUser?.delete()
    }
}