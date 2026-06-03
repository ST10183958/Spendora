package com.menak.login.ui.state

data class AnalyticsUiState(
    val totalSpent: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val minGoal: Double = 0.0,
    val maxGoal: Double = 0.0,
    val goalScore: Double = 0.0,
    val daysWithinGoalPercent: Double = 0.0,
    val daysOverGoalPercent: Double = 0.0
)