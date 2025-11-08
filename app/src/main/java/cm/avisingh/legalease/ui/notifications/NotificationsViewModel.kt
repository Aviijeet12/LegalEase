package cm.avisingh.legalease.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.notifications.InAppNotification
import cm.avisingh.legalease.notifications.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

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
            notificationRepository.getAllNotifications()
                .catch { e ->
                    _notificationState.value = NotificationState.Error(
                        e.message ?: "An error occurred"
                    )
                }
                .map { notifications ->
                    if (notifications.isEmpty()) {
                        NotificationState.Empty
                    } else {
                        NotificationState.Success(notifications)
                    }
                }
                .collect { state ->
                    _notificationState.value = state
                    _isLoading.value = false
                }
        }
    }

    fun markAsRead(notificationId: Long) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            notificationRepository.markAllAsRead()
        }
    }

    fun deleteNotification(notificationId: Long) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(notificationId)
        }
    }
}

sealed class NotificationState {
    object Loading : NotificationState()
    object Empty : NotificationState()
    data class Success(val notifications: List<InAppNotification>) : NotificationState()
    data class Error(val message: String) : NotificationState()
}