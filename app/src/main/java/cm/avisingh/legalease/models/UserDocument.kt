package cm.avisingh.legalease.models

data class UserDocument(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val fileSize: Long = 0,
    val downloadUrl: String = "",
    val storagePath: String = "",
    val uploadedAt: Long = 0,
    val uploadedBy: String = "",
    val status: String = "uploaded"
)
