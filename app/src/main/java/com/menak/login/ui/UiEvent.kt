package com.menak.login.ui

sealed class UiEvent {

    data class ShowSnackbar(
        val message: String
    ) : UiEvent()
}