package cm.avisingh.legalease.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthService {
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Define Result inside AuthService
    sealed class Result {
        data class Success(val user: FirebaseUser) : Result()
        data class Failure(val errorMessage: String) : Result()
    }

    suspend fun loginUser(email: String, password: String): Result {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Failure("Login failed - no user returned")
            }
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Login failed")
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}