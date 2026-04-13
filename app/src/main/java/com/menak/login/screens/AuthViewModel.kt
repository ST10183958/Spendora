package com.menak.login.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.menak.login.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun register() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                message = "Please enter username and password"
            )
            return
        }

        viewModelScope.launch {
            val result = repository.register(username, password)
            _uiState.value = _uiState.value.copy(
                message = result.fold(
                    onSuccess = { it },
                    onFailure = { it.message ?: "Registration failed" }
                )
            )
        }
    }

    fun login() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                message = "Please enter username and password"
            )
            return
        }

        viewModelScope.launch {
            val result = repository.login(username, password)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = true,
                        loggedInUsername = user.username,
                        message = "Login successful"
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        message = it.message ?: "Login failed"
                    )
                }
            )
        }
    }

    fun logout() {
        _uiState.value = AuthUiState(message = "Logged out")
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}