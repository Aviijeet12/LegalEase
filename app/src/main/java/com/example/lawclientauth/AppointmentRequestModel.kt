package com.example.lawclientauth

data class AppointmentRequestModel(
    val id: String,
    val clientName: String,
    val date: String,            // yyyy-MM-dd
    val time: String,
    val meetingType: String,
    val reason: String,
    val clientDocs: List<String> = emptyList(),
    var status: String = "Pending" // Pending / Accepted / Rejected
)
