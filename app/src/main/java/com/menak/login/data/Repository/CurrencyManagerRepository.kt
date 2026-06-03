package com.menak.login.data.Repository

import android.content.Context
import com.menak.login.data.CurrencyType

class CurrencyManagerRepository (context : Context) {

    private val prefs =
        context.getSharedPreferences("budget_prefs", Context.MODE_PRIVATE)

    fun saveCurrency(currency: CurrencyType) {
        prefs.edit()
            .putString("currency", currency.name)
            .apply()
    }

    fun getCurrency(): CurrencyType {
        val value = prefs.getString("currency", CurrencyType.ZAR.name)
        return CurrencyType.valueOf(value!!)
    }
}