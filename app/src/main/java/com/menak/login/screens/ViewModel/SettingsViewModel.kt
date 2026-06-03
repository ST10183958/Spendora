package com.menak.login.screens.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.menak.login.data.Repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserPreferencesRepository(app.applicationContext)

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    init {
        viewModelScope.launch {
            repo.darkModeFlow.collect { enabled ->
                _darkMode.value = enabled
            }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
        viewModelScope.launch {
            repo.setDarkMode(enabled)
        }
    }

    fun toggleDarkMode() {
        setDarkMode(!_darkMode.value)
    }
}