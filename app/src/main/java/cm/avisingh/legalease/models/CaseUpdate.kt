package cm.avisingh.legalease.models

import java.text.SimpleDateFormat
import java.util.*

data class CaseUpdate(
    val id: String,
    val title: String,
    val description: String,
    val date: Date,
    val type: String, // "HEARING", "DOCUMENT", "NOTE", etc.
    val caseId: String
) {
    fun getTypeColor(): Int {
        return when (type) {
            "HEARING" -> R.color.hearing_color
            "DOCUMENT" -> R.color.document_color
            "NOTE" -> R.color.note_color
            else -> R.color.default_color
        }
    }

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        return sdf.format(date)
    }
}