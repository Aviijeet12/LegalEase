package com.example.lawclientauth

/**
 * Represents a contact in the in-app contact list.
 */
data class ContactModel(
    val id: String,
    val name: String,
    val role: String, // "lawyer" or "client"
    val lastMessage: String,
    val isOnline: Boolean
)
