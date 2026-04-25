package com.menak.login.ui

data class CategoryAnalyticsItem(
    val name: String,
    val amount: Double,
    val color: Long
)

data class DailySpendingItem(
    val date: String,
    val amount: Double
)

data class AnalyticsUiState(
    val totalSpent: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val categoryBreakdown: List<CategoryAnalyticsItem> = emptyList(),
    val dailySpending: List<DailySpendingItem> = emptyList(),
    val thisMonthTotal: Double = 0.0,
    val lastMonthTotal: Double = 0.0
)