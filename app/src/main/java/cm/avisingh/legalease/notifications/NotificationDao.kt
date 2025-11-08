package cm.avisingh.legalease.notifications

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cm.avisingh.legalease.security.InAppNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<InAppNotification>>

    @Query("SELECT * FROM notifications WHERE readAt IS NULL ORDER BY createdAt DESC")
    fun getUnreadNotifications(): Flow<List<InAppNotification>>

    @Query("SELECT COUNT(*) FROM notifications WHERE readAt IS NULL")
    fun getUnreadCount(): Flow<Int>

    @Insert
    suspend fun insertNotification(notification: InAppNotification)

    @Update
    suspend fun updateNotification(notification: InAppNotification)

    @Query("UPDATE notifications SET readAt = :readTime WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String, readTime: Long = System.currentTimeMillis())

    @Query("UPDATE notifications SET readAt = :readTime WHERE readAt IS NULL")
    suspend fun markAllAsRead(readTime: Long = System.currentTimeMillis())

    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: String)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
}