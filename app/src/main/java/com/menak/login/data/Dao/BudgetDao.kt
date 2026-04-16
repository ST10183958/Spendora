package com.menak.login.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menak.login.data.Entity.BudgetGoalEntity
import com.menak.login.data.Entity.CategoryBudgetLimitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBudgetGoal(goal: BudgetGoalEntity)

    @Query("SELECT * FROM budget_goal WHERE id = 1 LIMIT 1")
    fun getBudgetGoal(): Flow<BudgetGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategoryBudgetLimit(limit: CategoryBudgetLimitEntity)

    @Query("SELECT * FROM category_budget_limits ORDER BY id ASC")
    fun getAllCategoryBudgetLimits(): Flow<List<CategoryBudgetLimitEntity>>
}