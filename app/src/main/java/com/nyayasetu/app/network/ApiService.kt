package com.nyayasetu.app.network

import com.nyayasetu.app.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Authentication
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<ApiResponse<User>>

    @PUT("auth/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ApiResponse<User>>

    // Cases
    @GET("cases")
    suspend fun getCases(): Response<ApiResponse<List<Case>>>

    @GET("cases/{id}")
    suspend fun getCase(@Path("id") caseId: String): Response<ApiResponse<Case>>

    @POST("cases")
    suspend fun createCase(@Body request: CreateCaseRequest): Response<ApiResponse<Case>>

    @PUT("cases/{id}")
    suspend fun updateCase(@Path("id") caseId: String, @Body request: UpdateCaseRequest): Response<ApiResponse<Case>>

    @DELETE("cases/{id}")
    suspend fun deleteCase(@Path("id") caseId: String): Response<ApiResponse<Unit>>

    // Documents
    @GET("documents")
    suspend fun getDocuments(): Response<ApiResponse<List<Document>>>

    @Multipart
    @POST("documents")
    suspend fun uploadDocument(
        @Part file: MultipartBody.Part,
        @Part("caseId") caseId: RequestBody,
        @Part("name") name: RequestBody
    ): Response<ApiResponse<Document>>

    // Chat/Messages
    @GET("messages/{conversationId}")
    suspend fun getMessages(@Path("conversationId") conversationId: String): Response<ApiResponse<List<Message>>>

    @POST("messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<ApiResponse<Message>>

    // Lawyers
    @GET("lawyers")
    suspend fun getLawyers(): Response<ApiResponse<List<User>>>

    @GET("lawyers/{id}")
    suspend fun getLawyer(@Path("id") lawyerId: String): Response<ApiResponse<User>>
}

// Data classes for API requests/responses
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val userType: String,
    val phone: String = ""
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class UpdateProfileRequest(
    val name: String,
    val phone: String,
    val address: String,
    val specialization: String = "",
    val experience: Int = 0
)

data class CreateCaseRequest(
    val title: String,
    val description: String,
    val category: String,
    val priority: String = "medium"
)

data class UpdateCaseRequest(
    val title: String,
    val description: String,
    val status: String,
    val priority: String
)

data class SendMessageRequest(
    val receiverId: String,
    val content: String,
    val messageType: String = "text",
    val conversationId: String = ""
)