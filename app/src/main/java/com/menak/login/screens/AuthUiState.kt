package com.menak.login.screens

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val message: String = "",
    val isLoggedIn: Boolean = false,
    val loggedInUsername: String = ""
)
