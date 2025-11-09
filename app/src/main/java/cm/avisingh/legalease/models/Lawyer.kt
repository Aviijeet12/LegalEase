package cm.avisingh.legalease.models

data class Lawyer(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val experience: String = "",
    val rating: Float = 0f,
    val totalCases: Int = 0,
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val about: String = "",
    val languages: List<String> = emptyList(),
    val availability: String = "Available"
)
