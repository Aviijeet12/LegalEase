package cm.avisingh.legalease.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cm.avisingh.legalease.security.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "notification_preferences")

class NotificationPreferences(
    private val context: Context
) {
    private val dataStore = context.dataStore

    // Get notification settings
    fun getNotificationEnabled(type: NotificationType): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey("notification_${type.name}")] ?: true
    }

    fun getEmailNotificationsEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(EMAIL_NOTIFICATIONS)] ?: true
    }

    fun getPushNotificationsEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(PUSH_NOTIFICATIONS)] ?: true
    }

    fun getInAppNotificationsEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(IN_APP_NOTIFICATIONS)] ?: true
    }

    fun getFcmToken(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(FCM_TOKEN)]
    }

    // Update notification settings
    suspend fun setNotificationEnabled(type: NotificationType, enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("notification_${type.name}")] = enabled
        }
    }

    suspend fun setEmailNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(EMAIL_NOTIFICATIONS)] = enabled
        }
    }

    suspend fun setPushNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(PUSH_NOTIFICATIONS)] = enabled
        }
    }

    suspend fun setInAppNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(IN_APP_NOTIFICATIONS)] = enabled
        }
    }

    suspend fun updateFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(FCM_TOKEN)] = token
        }
    }

    companion object {
        private const val EMAIL_NOTIFICATIONS = "email_notifications"
        private const val PUSH_NOTIFICATIONS = "push_notifications"
        private const val IN_APP_NOTIFICATIONS = "in_app_notifications"
        private const val FCM_TOKEN = "fcm_token"
    }
}