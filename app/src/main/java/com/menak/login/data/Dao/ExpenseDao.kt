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
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("""
        SELECT * FROM expenses
        WHERE startDate >= :fromDate AND endDate <= :toDate
        ORDER BY startDate DESC
    """)
    fun getExpensesBetweenDates(fromDate: String, toDate: String): Flow<List<ExpenseEntity>>

    @Query("""
        SELECT 
            c.id AS categoryId,
            c.type AS categoryType,
            c.iconUrl AS categoryIconUrl,
            COALESCE(SUM(e.amount), 0) AS totalSpent
        FROM categories c
        LEFT JOIN expenses e
            ON c.id = e.categoryId
            AND e.startDate >= :fromDate
            AND e.endDate <= :toDate
        GROUP BY c.id, c.type, c.iconUrl
        ORDER BY c.type ASC
    """)
    fun getCategoryTotalsBetweenDates(
        fromDate: String,
        toDate: String
    ): Flow<List<CategorySpendingTotal>>
}