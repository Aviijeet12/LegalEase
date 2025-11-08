package cm.avisingh.legalease.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cm.avisingh.legalease.notifications.InAppNotification
import cm.avisingh.legalease.notifications.NotificationConverters
import cm.avisingh.legalease.notifications.NotificationDao

@Database(
    entities = [InAppNotification::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(NotificationConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "legalease.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}