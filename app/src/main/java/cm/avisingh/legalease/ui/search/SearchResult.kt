package cm.avisingh.legalease.ui.search

import android.view.View
import androidx.annotation.DrawableRes
import java.util.Date

enum class SearchResultType {
    GUIDE,
    FAQ,
    DOCUMENT,
    LAWYER
}

data class SearchResult(
    val id: String,
    val type: SearchResultType,
    val title: String,
    val description: String,
    val category: DocumentCategory,
    @DrawableRes val iconResId: Int,
    val matchText: String,
    val date: Date = Date(),
    val size: Long = 0, // File size in bytes
    val documentType: DocumentType? = null, // Only for DOCUMENT type
    var imageView: View? = null // For shared element transitions
)