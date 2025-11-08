package cm.avisingh.legalease.security

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "in_app_notifications")
data class InAppNotification(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val source: String,
    val signature: String?,
    val createdAt: Date,
    val readAt: Date?,
    val expiresAt: Date?,
    val priority: NotificationPriority = NotificationPriority.NORMAL,
    val type: NotificationType,
    val metadata: Map<String, String> = emptyMap(),
    val encryptedPayload: ByteArray? = null,
    val isEncrypted: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InAppNotification

        if (id != other.id) return false
        if (title != other.title) return false
        if (message != other.message) return false
        if (source != other.source) return false
        if (signature != other.signature) return false
        if (createdAt != other.createdAt) return false
        if (readAt != other.readAt) return false
        if (expiresAt != other.expiresAt) return false
        if (priority != other.priority) return false
        if (type != other.type) return false
        if (metadata != other.metadata) return false
        if (encryptedPayload != null) {
            if (other.encryptedPayload == null) return false
            if (!encryptedPayload.contentEquals(other.encryptedPayload)) return false
        } else if (other.encryptedPayload != null) return false
        if (isEncrypted != other.isEncrypted) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + (signature?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (readAt?.hashCode() ?: 0)
        result = 31 * result + (expiresAt?.hashCode() ?: 0)
        result = 31 * result + priority.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + metadata.hashCode()
        result = 31 * result + (encryptedPayload?.contentHashCode() ?: 0)
        result = 31 * result + isEncrypted.hashCode()
        return result
    }
}

enum class NotificationPriority {
    LOW, NORMAL, HIGH, URGENT
}

enum class NotificationType {
    GENERAL,
    CASE_UPDATE,
    DOCUMENT_READY,
    APPOINTMENT,
    DEADLINE,
    SECURITY
}