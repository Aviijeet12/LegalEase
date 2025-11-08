package cm.avisingh.legalease.ui.documents

import androidx.annotation.DrawableRes

data class Document(
    val id: String,
    val name: String,
    val description: String,
    val type: String,
    val size: String,
    val date: String,
    val category: DocumentCategory,
    @DrawableRes val iconResId: Int,
    var isSelected: Boolean = false
)