package com.example.lawclientauth

data class PaymentModel(
    val invoiceId: String,
    val lawyer: String,
    val type: String,
    val amount: Int,
    val date: String,
    val status: String,
    val invoiceUrl: String
)
