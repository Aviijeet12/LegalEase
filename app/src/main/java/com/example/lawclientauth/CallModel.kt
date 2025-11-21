package com.example.lawclientauth

data class CallModel(
    val id: String,
    val peerName: String,
    val type: String, // "voice" or "video"
    val timestamp: Long,
    val durationSec: Int,
    val status: String // "missed", "completed"
)
