package cm.avisingh.legalease.data.model

import com.google.firebase.Timestamp

data class Document(
    val id: String = "",
    val name: String = "",
    val url: String = "",
    val category: String = "",
    val description: String? = null,
    val uploadedBy: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val size: Long = 0,
    val mimeType: String? = null,
    val version: Int = 1,
    val tags: List<String> = emptyList(),
    val sharedWith: List<String> = emptyList(),
    val isPublic: Boolean = false,
    val metadata: Map<String, String> = emptyMap()
)