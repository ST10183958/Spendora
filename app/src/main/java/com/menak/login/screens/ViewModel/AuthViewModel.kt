package com.menak.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menak.login.data.Repository.FirebaseAuthRepository
import com.menak.login.screens.State.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val state: AuthUiState
        get() = _uiState.value


    fun onUsernameChange(value: String) {
        _uiState.value = state.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = state.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = state.copy(confirmPassword = value)
    }

    fun clearMessage() {
        _uiState.value = state.copy(message = "")
    }

    fun register() {
        val username = state.username.trim()
        val password = state.password.trim()
        val confirmPassword = state.confirmPassword.trim()

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _uiState.value = state.copy(message = "Please fill in all fields")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = state.copy(message = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            val result = repository.register(username, password)

            result.fold(
                onSuccess = {
                    _uiState.value = state.copy(
                        message = "Account created successfully",
                        isLoggedIn = true,
                        loggedInUsername = username,
                        password = "",
                        confirmPassword = ""
                    )
                },
                onFailure = {
                    _uiState.value = state.copy(
                        message = it.message ?: "Registration failed"
                    )
                }
            )
        }
    }


    fun login() {
        val username = state.username.trim()
        val password = state.password.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.value = state.copy(
                message = "Please enter username and password"
            )
            return
        }

        viewModelScope.launch {
            val result = repository.login(username, password)

            result.fold(
                onSuccess = { user ->
                    _uiState.value = state.copy(
                        isLoggedIn = true,
                        loggedInUsername = _uiState.value.username,
                        message = "Login successful",
                        password = ""
                    )
                },
                onFailure = {
                    _uiState.value = state.copy(
                        message = it.message ?: "Invalid username or password",
                        isLoggedIn = false
                    )
                }
            )
        }
    }

    fun logout() {
        _uiState.value = AuthUiState(
            message = "Logged out"
        )
    }
}