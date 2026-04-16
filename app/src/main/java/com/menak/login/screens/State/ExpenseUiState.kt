package com.menak.login.screens.State

import com.menak.login.data.Entity.BudgetGoalEntity
import com.menak.login.data.Entity.CategoryBudgetLimitEntity
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.CategorySpendingTotal
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
    val expenseIconUrl: String = "",
    val receiptPhotoUrl: String = "",

    val monthlyBudgetGoalInput: String = "",
    val selectedBudgetCategoryId: Int? = null,
    val categoryBudgetLimitInput: String = "",

    val periodFromDate: String = "",
    val periodToDate: String = "",

    val categories: List<CategoryEntity> = emptyList(),
    val expenses: List<ExpenseEntity> = emptyList(),
    val filteredExpenses: List<ExpenseEntity> = emptyList(),
    val categoryTotals: List<CategorySpendingTotal> = emptyList(),
    val budgetGoal: BudgetGoalEntity? = null,
    val categoryBudgetLimits: List<CategoryBudgetLimitEntity> = emptyList(),

    val message: String = ""
)