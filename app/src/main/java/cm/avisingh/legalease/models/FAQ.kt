package cm.avisingh.legalease.models

data class FAQ(
    val id: String,
    val question: String,
    val answer: String,
    val category: String,
    var isExpanded: Boolean = false
)
