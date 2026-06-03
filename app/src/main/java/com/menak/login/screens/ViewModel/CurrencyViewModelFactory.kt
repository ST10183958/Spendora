package com.menak.login.screens.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.menak.login.data.Repository.CurrencyManagerRepository

class CurrencyViewModelFactory(
    private val currencyManager: CurrencyManagerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(currencyManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}