package cm.avisingh.legalease.ui.search

data class FAQ(
    val id: String = "",
    val question: String = "",
    val answer: String = "",
    val category: String = "",
    val helpful: Int = 0,
    val views: Int = 0
)
