package com.example.lawclientauth

/**
 * Universal message model used by ChatActivity.
 *
 * - senderId: unique id for sender ("me" / other)
 * - type: "text" | "file" | "image"
 * - status: "sending" | "sent" | "delivered" | "read"  (UI-only)
 * - fileUrl: used for file/image messages. Using uploaded local path as mock URL: /mnt/data/le-z.zip
 */
data class MessageModel(
    val id: String = java.util.UUID.randomUUID().toString(),
    val senderId: String,           // "me" or other user id
    val text: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "text",
    var status: String = "sent",
    val fileUrl: String? = null     // path or url
)
