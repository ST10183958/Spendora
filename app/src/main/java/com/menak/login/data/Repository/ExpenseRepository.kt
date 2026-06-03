package com.menak.login.data.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.menak.login.data.Dao.BudgetDao
import com.menak.login.data.Dao.CategoryDao
import com.menak.login.data.Dao.ExpenseDao
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.CategorySpendingTotal
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val categoryDao: CategoryDao,
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao,
    private val firestore: FirebaseFirestore
) {


    suspend fun addCategory(type: String, iconUrl: String) {
        val category = CategoryEntity(
            type = type,
            iconUrl = iconUrl
        )

        categoryDao.insert(category)

        firestore.collection("categories")
            .add(
                mapOf(
                    "type" to type,
                    "iconUrl" to iconUrl
                )
            )
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> =
        categoryDao.getAllCategories()


    suspend fun addExpense(expense: ExpenseEntity) {
        expenseDao.insert(expense)

        firestore.collection("expenses")
            .add(
                mapOf(
                    "name" to expense.name,
                    "categoryId" to expense.categoryId,
                    "amount" to expense.amount,
                    "startDate" to expense.startDate,
                    "endDate" to expense.endDate,
                    "description" to expense.description,
                    "expenseIconUrl" to expense.expenseIconUrl,
                    "receiptPhotoUrl" to expense.receiptPhotoUrl
                )
            )
    }

    fun getAllExpenses(): Flow<List<ExpenseEntity>> =
        expenseDao.getAllExpenses()

    fun getExpensesBetweenDates(from: String, to: String): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesBetweenDates(from, to)

    fun getCategoryTotalsBetweenDates(
        from: String,
        to: String
    ): Flow<List<CategorySpendingTotal>> =
        expenseDao.getCategoryTotalsBetweenDates(from, to)


    suspend fun saveMonthlyBudgetGoal(amount: Double) {
        budgetDao.upsertBudgetGoal(
            com.menak.login.data.Entity.BudgetGoalEntity(
                id = 1,
                monthlyTotalBudget = amount   // ✅ FIXED NAME
            )
        )
    }

    suspend fun saveCategoryBudgetLimit(categoryId: Int, limit: Double) {
        budgetDao.upsertCategoryBudgetLimit(
            com.menak.login.data.Entity.CategoryBudgetLimitEntity(
                categoryId = categoryId,
                monthlyLimit = limit
            )
        )
    }
}