package com.example.lawclientauth

data class NotificationModel(
    val title: String,
    val message: String,
    val time: String,
    val type: String,      // appointment, payment, document, message, system
    var isUnread: Boolean
)
