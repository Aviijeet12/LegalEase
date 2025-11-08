package cm.avisingh.legalease.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val profileImage: String?,
    val verified: Boolean = false,
    val createdAt: Date,
    val updatedAt: Date
)

enum class UserRole {
    CLIENT, LAWYER, ADMIN
}

@Entity(tableName = "cases")
data class Case(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val clientId: String,
    val lawyerId: String?,
    val status: CaseStatus,
    val category: CaseCategory,
    val nextHearingDate: Date?,
    val court: String?,
    val caseNumber: String?,
    val createdAt: Date,
    val updatedAt: Date
)

enum class CaseStatus {
    NEW, ASSIGNED, IN_PROGRESS, ON_HOLD, CLOSED
}

enum class CaseCategory {
    CIVIL, CRIMINAL, CORPORATE, FAMILY, PROPERTY, TAXATION, OTHER
}

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey
    val id: String,
    val caseId: String,
    val name: String,
    val type: DocumentType,
    val url: String,
    val uploadedBy: String,
    val createdAt: Date
)

enum class DocumentType {
    COURT_ORDER, PETITION, EVIDENCE, CONTRACT, ID_PROOF, OTHER
}

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val id: String,
    val caseId: String,
    val senderId: String,
    val content: String,
    val type: MessageType,
    val documentUrl: String?,
    val createdAt: Date,
    val read: Boolean = false
)

enum class MessageType {
    TEXT, DOCUMENT, IMAGE, VOICE
}

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey
    val id: String,
    val caseId: String,
    val clientId: String,
    val lawyerId: String,
    val date: Date,
    val duration: Int, // in minutes
    val type: AppointmentType,
    val status: AppointmentStatus,
    val notes: String?
)

enum class AppointmentType {
    VIDEO_CALL, IN_PERSON, PHONE_CALL
}

enum class AppointmentStatus {
    SCHEDULED, CONFIRMED, COMPLETED, CANCELLED
}

@Entity(tableName = "case_updates")
data class CaseUpdate(
    @PrimaryKey
    val id: String,
    val caseId: String,
    val title: String,
    val description: String,
    val updateType: UpdateType,
    val createdAt: Date,
    val createdBy: String
)

enum class UpdateType {
    HEARING_UPDATE, DOCUMENT_FILED, STATUS_CHANGE, NOTE_ADDED
}