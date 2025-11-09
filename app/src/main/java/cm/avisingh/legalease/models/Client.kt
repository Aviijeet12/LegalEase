package cm.avisingh.legalease.models

data class Client(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val activeCases: Int = 0,
    val totalCases: Int = 0,
    val status: String = "Active"
)
