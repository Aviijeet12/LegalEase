package com.nyayasetu.app.payment

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Example payment request/response

data class PaymentRequest(val userId: String, val lawyerId: String, val amount: Double)
data class PaymentResponse(val success: Boolean, val transactionId: String?, val error: String?)

interface PaymentApiService {
    @POST("/api/payment")
    fun makePayment(@Body request: PaymentRequest): Call<PaymentResponse>
}
