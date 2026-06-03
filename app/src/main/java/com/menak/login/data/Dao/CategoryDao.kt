package com.menak.login.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menak.login.data.Entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    // Insert or replace category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    // Get all categories
    @Query("SELECT * FROM categories ORDER BY id ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    // Get single category (optional but useful)
    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    fun getCategoryById(id: Int): Flow<CategoryEntity?>

    // Delete category
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategory(id: Int)
}