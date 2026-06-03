package com.menak.login.data.Entity

import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName="category_budget_limits",

    foreignKeys = [

        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class CategoryBudgetLimitEntity(
@PrimaryKey
    val id: Int = 0,
    val categoryId: Int,
    val monthlyLimit: Double
)