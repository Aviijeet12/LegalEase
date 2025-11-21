package com.example.lawclientauth

/**
 * Represents a single call log entry.
 *
 * type = "audio" | "video"
 * status = "missed" | "incoming" | "outgoing"
 */
data class CallLogModel(
    val name: String,
    val time: Long,
    val duration: Long, // in seconds (0 for missed)
    val type: String,   // audio/video
    val status: String  // incoming/outgoing/missed
)
