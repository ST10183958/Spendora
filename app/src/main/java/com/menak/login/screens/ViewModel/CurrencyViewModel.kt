package com.menak.login.screens.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.menak.login.data.CurrencyType
import com.menak.login.data.Repository.CurrencyManagerRepository

class CurrencyViewModel(
    private val currencyManager: CurrencyManagerRepository
) : ViewModel() {

    private val _currency = mutableStateOf(currencyManager.getCurrency())
    val currency: State<CurrencyType> = _currency

    fun updateCurrency(newCurrency: CurrencyType) {
        currencyManager.saveCurrency(newCurrency)
        _currency.value = newCurrency
    }
}