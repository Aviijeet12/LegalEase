package cm.avisingh.legalease.data.dao

import androidx.room.*
import cm.avisingh.legalease.security.InAppNotification
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface NotificationDao {
    @Query("SELECT * FROM in_app_notifications ORDER BY createdAt DESC")
    fun getAllNotifications(): Flow<List<InAppNotification>>

    @Query("SELECT * FROM in_app_notifications WHERE readAt IS NULL ORDER BY createdAt DESC")
    fun getUnreadNotifications(): Flow<List<InAppNotification>>

    @Query("SELECT * FROM in_app_notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): InAppNotification?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: InAppNotification)

    @Update
    suspend fun update(notification: InAppNotification)

    @Delete
    suspend fun delete(notification: InAppNotification)

    @Query("DELETE FROM in_app_notifications WHERE expiresAt < :now")
    suspend fun deleteExpiredNotifications(now: Date)
    
    @Query("SELECT COUNT(*) FROM in_app_notifications WHERE readAt IS NULL")
    suspend fun getUnreadCount(): Int
}