package cm.avisingh.legalease.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val accountType: String = "client" // client, lawyer, admin

)

data class Document(
    val _id: String,
    val title: String,
    val type: String,
    val content: String,
    val createdAt: String
)