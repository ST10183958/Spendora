package com.menak.login.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menak.login.data.Entity.CategorySpendingTotal
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("""
        SELECT * FROM expenses 
        WHERE startDate BETWEEN :from AND :to
        ORDER BY startDate DESC
    """)
    fun getExpensesBetweenDates(
        from: String,
        to: String
    ): Flow<List<ExpenseEntity>>

    @Query("""
        SELECT categoryId AS categoryId,
               SUM(amount) AS total
        FROM expenses
        WHERE startDate BETWEEN :from AND :to
        GROUP BY categoryId
    """)
    fun getCategoryTotalsBetweenDates(
        from: String,
        to: String
    ): Flow<List<CategorySpendingTotal>>
}