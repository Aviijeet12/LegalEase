package cm.avisingh.legalease.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.data.model.FirebaseDocument
import cm.avisingh.legalease.security.InAppNotification
import cm.avisingh.legalease.data.repository.DocumentRepository
import cm.avisingh.legalease.data.repository.NotificationRepository
import cm.avisingh.legalease.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val documentRepository = DocumentRepository(application)
    // TODO: Inject NotificationRepository properly when dependencies are set up
    // private val notificationRepository = NotificationRepository()
    private val userRepository = UserRepository()

    private val _userStats = MutableStateFlow<UserStats>(UserStats())
    val userStats: StateFlow<UserStats> = _userStats

    private val _recentDocuments = MutableStateFlow<List<FirebaseDocument>>(emptyList())
    val recentDocuments: StateFlow<List<FirebaseDocument>> = _recentDocuments

    private val _notifications = MutableStateFlow<List<InAppNotification>>(emptyList())
    val notifications: StateFlow<List<InAppNotification>> = _notifications

    private val _storageStats = MutableStateFlow<StorageStats>(StorageStats())
    val storageStats: StateFlow<StorageStats> = _storageStats

    fun loadUserStats() {
        viewModelScope.launch {
            val userId = userRepository.currentUser?.uid ?: return@launch
            val docs = documentRepository.getDocuments(userId)
            val sharedDocs = docs.filter { it.sharedWith.isNotEmpty() }
            
            _userStats.value = UserStats(
                userName = userRepository.currentUser?.displayName ?: "User",
                totalDocuments = docs.size,
                sharedDocuments = sharedDocs.size
            )
        }
    }

    fun loadRecentDocuments() {
        viewModelScope.launch {
            val userId = userRepository.currentUser?.uid ?: return@launch
            val docs = documentRepository.getRecentDocuments(userId, limit = 10)
            _recentDocuments.value = docs
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            // TODO: Load notifications when NotificationRepository is properly injected
            _notifications.value = emptyList()
        }
    }

    fun loadStorageStats() {
        viewModelScope.launch {
            val userId = userRepository.currentUser?.uid ?: return@launch
            val totalStorage = 10L * 1024 * 1024 * 1024 // 10GB
            val usedStorage = documentRepository.getDocuments(userId)
                .sumOf { it.size }

            _storageStats.value = StorageStats(
                usedStorage = formatFileSize(usedStorage),
                totalStorage = formatFileSize(totalStorage),
                usagePercentage = ((usedStorage.toDouble() / totalStorage) * 100).toInt()
            )
        }
    }

    private fun formatFileSize(size: Long): String {
        val kb = size / 1024.0
        return when {
            kb < 1024 -> String.format("%.1f KB", kb)
            kb < 1024 * 1024 -> String.format("%.1f MB", kb / 1024)
            else -> String.format("%.1f GB", kb / (1024 * 1024))
        }
    }
}

data class UserStats(
    val userName: String = "",
    val totalDocuments: Int = 0,
    val sharedDocuments: Int = 0
)

data class StorageStats(
    val usedStorage: String = "0 KB",
    val totalStorage: String = "0 GB",
    val usagePercentage: Int = 0
)