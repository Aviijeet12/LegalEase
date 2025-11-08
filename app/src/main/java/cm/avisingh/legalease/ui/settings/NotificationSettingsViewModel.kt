package cm.avisingh.legalease.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.notifications.NotificationPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationSettingsViewModel : ViewModel() {

    private val _notificationPreferences = MutableStateFlow(NotificationPreferences())
    val notificationPreferences: StateFlow<NotificationPreferences> = _notificationPreferences.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        // Load from SharedPreferences or Firebase
        _notificationPreferences.value = NotificationPreferences()
    }

    fun updatePreferences(update: (NotificationPreferences) -> NotificationPreferences) {
        val newPreferences = update(_notificationPreferences.value)
        _notificationPreferences.value = newPreferences
        
        viewModelScope.launch {
            // TODO: Save to backend when NotificationRepository is fully implemented
        }
    }
}