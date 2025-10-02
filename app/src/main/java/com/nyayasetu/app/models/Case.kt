package com.nyayasetu.app.models

data class Case(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val lawyerId: String = "",
    val lawyerName: String = "",
    val status: String = "", // "pending", "active", "closed", "cancelled"
    val priority: String = "", // "low", "medium", "high"
    val category: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val lastUpdate: String = "",
    val nextHearing: String = "",
    val nextHearingDate: String = "",
    val courtName: String = "",
    val caseNumber: String = "",
    val documents: List<Document> = emptyList()
)