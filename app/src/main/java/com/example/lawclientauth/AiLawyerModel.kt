package com.example.lawclientauth

data class AiLawyerModel(
    val name: String,
    val specialization: String,
    val rating: Double,
    val price: Int,
    val experience: Int,
    val matchScore: Int
)
