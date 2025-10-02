package com.nyayasetu.app.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val userType: String = "", // "client", "lawyer", "admin"
    val role: String = "", // "client", "lawyer", "admin"
    val profilePicture: String = "",
    val address: String = "",
    val specialization: String = "", // for lawyers
    val lawyerSpecialty: String = "", // for lawyers
    val experience: Int = 0, // for lawyers
    val lawyerExperience: String = "", // for lawyers
    val rating: Float = 0f, // for lawyers
    val licenseNumber: String = "", // for lawyers
    val isVerified: Boolean = false,
    val createdAt: String = ""
)