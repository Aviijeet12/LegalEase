package cm.avisingh.legalease.models

import java.text.SimpleDateFormat
import java.util.*
import cm.avisingh.legalease.R



data class TimelineEvent(
    val id: String,
    val title: String,
    val description: String,
    val date: Date,
    val eventType: String, // "FILED", "HEARING", "JUDGMENT", "APPEAL", etc.
    val caseId: String
) {
    fun getEventTypeColor(): Int {
        return when (eventType) {
            "FILED" -> R.color.filed_color
            "HEARING" -> R.color.hearing_color
            "JUDGMENT" -> R.color.judgment_color
            "APPEAL" -> R.color.appeal_color
            "DOCUMENT" -> R.color.document_color
            "INSPECTION" -> R.color.inspection_color
            else -> R.color.default_color
        }
    }

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}