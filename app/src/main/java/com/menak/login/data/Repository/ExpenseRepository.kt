package com.menak.login.data.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.menak.login.data.Dao.BudgetDao
import com.menak.login.data.Dao.CategoryDao
import com.menak.login.data.Dao.ExpenseDao
import com.menak.login.data.Entity.BudgetGoalEntity
import com.menak.login.data.Entity.CategoryBudgetLimitEntity
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class ExpenseRepository(
    private val categoryDao: CategoryDao,
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao,
    private val firestore: FirebaseFirestore
) {

    // -------------------------
    // Categories
    // -------------------------
    fun getAllCategories(): Flow<List<CategoryEntity>> =
        categoryDao.getAllCategories()

    fun addCategory(type: String, iconUrl: String) {
        runBlocking {
            categoryDao.insert(
                CategoryEntity(
                    type = type,
                    iconUrl = iconUrl
                )
            )
        }
    }

    // -------------------------
    // Expenses
    // -------------------------
    fun getAllExpenses(): Flow<List<ExpenseEntity>> =
        expenseDao.getAllExpenses()

    fun addExpense(
        name: String,
        categoryId: Int,
        amount: Double,
        startDate: String,
        endDate: String,
        description: String,
        expenseIconUri: String,
        receiptPhotoUrl: String
    ) {
        runBlocking {
            expenseDao.insert(
                ExpenseEntity(
                    name = name,
                    categoryId = categoryId,
                    amount = amount,
                    startDate = startDate,
                    endDate = endDate,
                    description = description,
                    expenseIconUri = expenseIconUri,
                    receiptPhotoUrl = receiptPhotoUrl
                )
            )
        }
    }

    fun getExpensesBetweenDates(from: String, to: String): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesBetweenDates(from, to)

    // -------------------------
    // Budget
    // -------------------------
    fun getBudgetGoal() =
        budgetDao.getBudgetGoal()

    fun getAllCategoryBudgetLimits() =
        budgetDao.getAllCategoryBudgetLimits()

    fun saveMonthlyBudgetGoal(amount: Double) {
        runBlocking {
            budgetDao.upsertBudgetGoal(
                BudgetGoalEntity(
                    id = 1,
                    monthlyTotalBudget = amount
                )
            )
        }
    }

    fun saveCategoryBudgetLimit(categoryId: Int, limit: Double) {
        runBlocking {
            budgetDao.upsertCategoryBudgetLimit(
                CategoryBudgetLimitEntity(
                    id = 0,
                    categoryId = categoryId,
                    monthlyLimit = limit
                )
            )
        }
    }

    fun getCategoryTotalsBetweenDates(from: String, to: String) =
        budgetDao.getCategoryTotalsBetweenDates(from, to)
}