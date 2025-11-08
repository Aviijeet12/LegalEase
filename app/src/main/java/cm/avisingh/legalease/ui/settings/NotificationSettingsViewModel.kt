package cm.avisingh.legalease.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.notifications.NotificationPreferences
import cm.avisingh.legalease.notifications.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _notificationPreferences = MutableStateFlow(NotificationPreferences())
    val notificationPreferences: StateFlow<NotificationPreferences> = _notificationPreferences.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        // In a real app, you would load this from Firebase
        // For now, we'll use default values
        _notificationPreferences.value = NotificationPreferences()
    }

    suspend fun updatePreferences(update: (NotificationPreferences) -> NotificationPreferences) {
        val newPreferences = update(_notificationPreferences.value)
        _notificationPreferences.value = newPreferences
        
        viewModelScope.launch {
            notificationRepository.updateNotificationPreferences(newPreferences)
        }
    }
}