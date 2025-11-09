package cm.avisingh.legalease.models

data class CaseStatus(
    val id: String = "",
    val caseNumber: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val category: String = "",
    val lawyerId: String = "",
    val lawyerName: String = "",
    val clientId: String = "",
    val createdAt: Long = 0,
    val lastUpdated: Long = 0,
    val nextHearing: Long = 0
)
