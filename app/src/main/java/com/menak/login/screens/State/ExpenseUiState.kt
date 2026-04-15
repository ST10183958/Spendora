package com.menak.login.screens.State

import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.ExpenseEntity

data class ExpenseUiState(
    val categoryType: String = "",
    val categoryIconUri: String = "",

    val expenseName: String = "",
    val selectedCategoryId: Int? = null,
    val expenseAmount: String = "",
    val expenseStartDate: String = "",
    val expenseEndDate: String = "",
    val expenseDescription: String = "",
    val expenseIconIrl: String = "",

    val categories: List<CategoryEntity> = emptyList(),
    val expenses: List<ExpenseEntity> = emptyList(),
    val message: String = ""
)