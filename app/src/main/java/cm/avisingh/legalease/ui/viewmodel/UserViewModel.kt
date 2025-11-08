package cm.avisingh.legalease.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.data.model.User
import cm.avisingh.legalease.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        // Load current user if logged in
        repository.currentUser?.let { firebaseUser ->
            loadUser(firebaseUser.uid)
        }
    }

    private fun loadUser(userId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val user = repository.getUser(userId)
                _currentUser.value = user
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.createUser(user)
                _currentUser.value = user
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.updateUser(user)
                _currentUser.value = user
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _currentUser.value = null
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.deleteAccount()
                _currentUser.value = null
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}