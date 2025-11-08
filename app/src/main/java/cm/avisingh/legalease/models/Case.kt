package cm.avisingh.legalease.models

import java.util.*
import cm.avisingh.legalease.R

data class Case(
    val id: String,
    val caseNumber: String,
    val title: String,
    val clientName: String,
    val clientEmail: String, // Add this
    val lawyerId: String,
    val caseType: String,
    val status: String,
    val priority: String,
    val description: String,
    val nextHearingDate: Date? = null,
    val caseFiledDate: Date, // Make sure this is a val/var
    val courtName: String? = null,
    val assignedLawyer: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {

    fun getStatusColor(): Int {
        return when (status.uppercase()) {
            "ACTIVE" -> R.color.green
            "PENDING" -> R.color.orange
            "CLOSED" -> R.color.gray
            "URGENT" -> R.color.red
            else -> R.color.gray
        }
    }

    fun getPriorityColor(): Int {
        return when (priority.uppercase()) {
            "HIGH" -> R.color.red
            "MEDIUM" -> R.color.orange
            "LOW" -> R.color.green
            else -> R.color.gray
        }
    }

    fun getFormattedFiledDate(): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(caseFiledDate)
    }

    fun getFormattedHearingDate(): String? {
        return nextHearingDate?.let {
            val sdf = java.text.SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
            sdf.format(it)
        }
    }
}