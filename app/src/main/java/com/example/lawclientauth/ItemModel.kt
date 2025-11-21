package com.example.lawclientauth.models

data class ItemModel(
    var title: String = "",
    var description: String = "",
    var createdAt: Long = 0L,
    var ownerId: String = ""
)
