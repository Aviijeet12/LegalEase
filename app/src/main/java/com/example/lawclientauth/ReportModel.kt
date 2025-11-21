package com.example.lawclientauth

/**
 * Represents a report filed by a client or a lawyer.
 */
data class ReportModel(
    val id: String,
    val title: String,
    val description: String,
    val filedBy: String, // lawyer or client name
    val category: String, // Abuse, Harassment, Fraud, Payment Issue, etc.
    val time: Long,
    var status: String // "Pending" | "Resolved"
)
