package cm.avisingh.legalease.models

data class LawyerCase(
    val id: String = "",
    val caseNumber: String = "",
    val title: String = "",
    val clientName: String = "",
    val caseType: String = "",
    val court: String = "",
    val nextHearing: Long = 0,
    val status: String = "Active",
    val priority: String = "Medium"
)
