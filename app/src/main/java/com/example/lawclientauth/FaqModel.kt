package com.example.lawclientauth

data class FaqModel(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)
