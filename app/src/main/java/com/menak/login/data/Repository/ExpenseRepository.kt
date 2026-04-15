package com.menak.login.data.Repository

import com.menak.login.data.Dao.CategoryDao
import com.menak.login.data.Dao.ExpenseDao
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val categoryDao: CategoryDao,
    private val expenseDao: ExpenseDao
) {
    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    suspend fun addCategory(type: String, iconUrl: String) {
        categoryDao.insertCategory(
            CategoryEntity(
                type = type,
                iconUrl = iconUrl
            )
        )
    }

    // Add expense input fields
    suspend fun addExpense(
        name: String,
        categoryId: Int,
        amount: Double,
        startDate: String,
        endDate: String,
        description: String,
        expenseIconUrl: String
    ) {
        expenseDao.insertExpense(
            ExpenseEntity(
                expenseName = name,
                categoryId = categoryId,
                amount = amount,
                startDate = startDate,
                endDate = endDate,
                description = description,
                expenseIconUrl = expenseIconUrl
            )
        )
    }
}