package com.menak.login.data

enum class CurrencyType(
    val code: String,
    val symbol: String
) {
    ZAR("ZAR", "R"),
    GBP("GBP", "£"),
    USD("USD", "$"),
    EUR("EUR", "€")
}