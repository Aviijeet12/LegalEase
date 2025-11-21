package com.example.lawclientauth

data class AppointmentModel(
    val clientName: String,
    val purpose: String,
    val date: String,
    val time: String,
    val meetingType: String, // Video Call / Audio Call / In-Person
    var status: String       // Upcoming / Completed / Cancelled
)
