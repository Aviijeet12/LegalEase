package com.nyayasetu.app.models

data class Document(
    val id: String = "",
    val name: String = "",
    val fileName: String = "",
    val title: String = "",
    val documentTitle: String = "",
    val url: String = "",
    val type: String = "", // "pdf", "image", "doc"
    val fileType: String = "",
    val documentType: String = ""
    val size: Long = 0,
    val documentSize: String = "",
    val uploadedBy: String = "",
    val uploadedAt: String = "",
    val uploadDate: String = "",
    val caseId: String = "",
    val isPublic: Boolean = false
)