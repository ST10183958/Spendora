package com.menak.login.data.Entity

import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName="category_budget_limits",
    //depends on foreign table
    foreignKeys = [
        //define the foreign keys
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            //childColumn stores parentColumns value in this table
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