package com.menak.login.ui

sealed class UiEventf {

    data class ShowSnackbar(
        val message: String
    ) : UiEvent()
}