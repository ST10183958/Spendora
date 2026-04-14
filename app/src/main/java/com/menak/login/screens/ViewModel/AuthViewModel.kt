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
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        message = "Account created successfully. You can now use the app.",
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