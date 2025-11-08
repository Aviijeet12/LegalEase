package cm.avisingh.legalease.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cm.avisingh.legalease.services.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authService = AuthService()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun loginUser(email: String, password: String, role: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authService.loginUser(email, password)) {
                is AuthService.Result.Success -> {
                    _authState.value = AuthState.Success(role)
                }
                is AuthService.Result.Failure -> {
                    _authState.value = AuthState.Error(result.errorMessage)
                }
            }
        }
    }

    fun registerUser(email: String, password: String, fullName: String, role: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authService.registerUser(email, password, fullName, role)) {
                is AuthService.Result.Success -> {
                    _authState.value = AuthState.Success(role)
                }
                is AuthService.Result.Failure -> {
                    _authState.value = AuthState.Error(result.errorMessage)
                }
            }
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val role: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}