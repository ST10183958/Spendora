package com.menak.login.data.Entity

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "budget_goal")
class BudgetGoalEntity (
    @PrimaryKey
    val id: Int = 1,
    val monthlyTotalBudget: Double
)