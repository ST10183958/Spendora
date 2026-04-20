package com.menak.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.menak.login.data.Repository.AuthRepository
import com.menak.login.screens.State.AuthUiState
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

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = "")
    }

    fun register() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password.trim()
        val confirmPassword = _uiState.value.confirmPassword.trim()

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                message = "Please fill in all fields"
            )
            return
        }

        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(
                message = "Passwords do not match"
            )
            return
        }

        viewModelScope.launch {
            val result = repository.register(username, password)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        message = "Account created successfully",
                        isLoggedIn = true,
                        loggedInUsername = username
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        message = it.message ?: "Registration failed"
                    )
                }
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
                        message = it.message ?: "Invalid username or password"
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
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}