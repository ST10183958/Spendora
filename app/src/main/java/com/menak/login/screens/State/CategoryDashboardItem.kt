package com.menak.login.ui

data class CategoryDashboardItem(
    val categoryId: Int,
    val categoryName: String,
    val spentAmount: Double,
    val remainingAmount: Double?,
    val progress: Float
)