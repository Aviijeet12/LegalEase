package com.nyayasetu.app.payment

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PaymentApiClient {
    // Ensure HTTPS is always used for payment API calls
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-payment-api.com/") // Use a real HTTPS endpoint for production
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: PaymentApiService = retrofit.create(PaymentApiService::class.java)
}
