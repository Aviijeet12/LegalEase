package cm.avisingh.legalease.models

data class Appointment(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val lawyerName: String = "",
    val dateTime: Long = 0,
    val duration: Int = 0, // minutes
    val type: String = "", // In-Person, Video Call, Phone Call
    val status: String = "", // Confirmed, Pending, Cancelled, Completed
    val location: String = ""
)
