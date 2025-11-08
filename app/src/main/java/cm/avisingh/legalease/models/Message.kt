package cm.avisingh.legalease.models

import java.util.Date

data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Date = Date(),
    val messageType: String = "text", // text, image, document
    val fileUrl: String? = null,
    val fileName: String? = null,
    val isRead: Boolean = false
) {
    // Firebase requires empty constructor
    constructor() : this("", "", "", "", "", Date(), "text", null, null, false)
}