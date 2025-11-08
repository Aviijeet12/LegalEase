package cm.avisingh.legalease.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.security.InAppNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private val _notificationState = MutableStateFlow<NotificationState>(NotificationState.Loading)
    val notificationState: StateFlow<NotificationState> = _notificationState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadNotifications()
    }

    fun refreshNotifications() {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            // TODO: Load from repository when NotificationRepository is complete
            _notificationState.value = NotificationState.Empty
            _isLoading.value = false
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            // TODO: Implement when NotificationRepository is complete
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            // TODO: Implement when NotificationRepository is complete
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            // TODO: Implement when NotificationRepository is complete
        }
    }
}

sealed class NotificationState {
    object Loading : NotificationState()
    object Empty : NotificationState()
    data class Success(val notifications: List<InAppNotification>) : NotificationState()
    data class Error(val message: String) : NotificationState()
}
