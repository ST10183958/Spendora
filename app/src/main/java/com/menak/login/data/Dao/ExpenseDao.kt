package com.menak.login.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // Insert expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    // Get all expenses
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    // Delete expense
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpense(id: Int)

    // Get expenses between dates (FOR ANALYTICS)
    @Query("""
        SELECT * FROM expenses
        WHERE startDate BETWEEN :from AND :to
        ORDER BY startDate ASC
    """)
    fun getExpensesBetweenDates(
        from: String,
        to: String
    ): Flow<List<ExpenseEntity>>

    // Get expenses by category (useful for filtering UI)
    @Query("""
        SELECT * FROM expenses
        WHERE categoryId = :categoryId
        ORDER BY startDate DESC
    """)
    fun getExpensesByCategory(categoryId: Int): Flow<List<ExpenseEntity>>
}