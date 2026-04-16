package com.menak.login.data.Repository

import com.menak.login.data.Dao.BudgetDao
import com.menak.login.data.Dao.CategoryDao
import com.menak.login.data.Dao.ExpenseDao
import com.menak.login.data.Entity.BudgetGoalEntity
import com.menak.login.data.Entity.CategoryBudgetLimitEntity
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.CategorySpendingTotal
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow


class ExpenseRepository(
    private val categoryDao: CategoryDao,
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao
) {
    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    fun getBudgetGoal(): Flow<BudgetGoalEntity?> = budgetDao.getBudgetGoal()

    fun getAllCategoryBudgetLimits(): Flow<List<CategoryBudgetLimitEntity>> =
        budgetDao.getAllCategoryBudgetLimits()

    fun getExpensesBetweenDates(fromDate: String, toDate: String): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesBetweenDates(fromDate, toDate)

    fun getCategoryTotalsBetweenDates(fromDate: String, toDate: String): Flow<List<CategorySpendingTotal>> =
        expenseDao.getCategoryTotalsBetweenDates(fromDate, toDate)

    suspend fun addCategory(type: String, iconUrl: String) {
        categoryDao.insertCategory(
            CategoryEntity(
                type = type,
                iconUrl = iconUrl
            )
        )
    }

    suspend fun addExpense(
        name: String,
        categoryId: Int,
        amount: Double,
        startDate: String,
        endDate: String,
        description: String,
        expenseIconUri: String,
        receiptPhotoUrl: String
    ) {
        expenseDao.insertExpense(
            ExpenseEntity(
                expenseName = name,
                categoryId = categoryId,
                amount = amount,
                startDate = startDate,
                endDate = endDate,
                description = description,
                expenseIconUrl = expenseIconUri,
                receiptPhotoUrl = receiptPhotoUrl
            )
        )
    }

    suspend fun saveMonthlyBudgetGoal(amount: Double) {
        budgetDao.upsertBudgetGoal(
            BudgetGoalEntity(
                id = 1,
                monthlyTotalBudget = amount
            )
        )
    }

    suspend fun saveCategoryBudgetLimit(categoryId: Int, limit: Double) {
        budgetDao.upsertCategoryBudgetLimit(
            CategoryBudgetLimitEntity(
                categoryId = categoryId,
                monthlyLimit = limit
            )
        )
    }
}