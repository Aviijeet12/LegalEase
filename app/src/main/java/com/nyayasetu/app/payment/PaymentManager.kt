package com.nyayasetu.app.payment

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PaymentManager {
    fun makePayment(userId: String, lawyerId: String, amount: Double, onResult: (Boolean, String?) -> Unit) {
        val request = PaymentRequest(userId, lawyerId, amount)
        PaymentApiClient.service.makePayment(request).enqueue(object : Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                val body = response.body()
                if (body != null && body.success) {
                    onResult(true, body.transactionId)
                } else {
                    onResult(false, body?.error ?: "Unknown error")
                }
            }
            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }
}
