package com.nyayasetu.app.models

data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val content: String = "",
    val timestamp: String = "",
    val messageType: String = "", // "text", "file", "image"
    val isRead: Boolean = false,
    val conversationId: String = "",
    val attachmentUrl: String = ""
)