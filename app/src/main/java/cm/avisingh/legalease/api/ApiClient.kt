package cm.avisingh.legalease.api

import cm.avisingh.legalease.models.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

object ApiClient {
    const val BASE_URL = "http://13.201.22.65:3000/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)  // Increased from 30 to 60 seconds
        .readTimeout(60, TimeUnit.SECONDS)     // Increased from 30 to 60 seconds
        .writeTimeout(60, TimeUnit.SECONDS)    // Added write timeout
        .retryOnConnectionFailure(true)        // Added retry
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginData>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<LoginData>

    @GET("api/cases")
    suspend fun getCases(): ApiResponse<List<Case>>

    @GET("api/documents")
    suspend fun getDocuments(): ApiResponse<List<Document>>

    @GET("/api/health")
    suspend fun healthCheck(): ApiResponse<String>

    // ... your existing endpoints

}

// Request models
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String)

// Response models
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)

data class LoginData(
    val token: String,
    val user: User
)