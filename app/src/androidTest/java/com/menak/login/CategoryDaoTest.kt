package com.menak.login.data.Dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.menak.login.data.AppDatabase
import com.menak.login.data.Entity.CategoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        categoryDao = database.categoryDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun testCategoryInsertedCorrectly() = runBlocking {
        // Arrange
        val category = CategoryEntity(type = "Entertainment", iconUrl = "entertainment_icon")

        // Act
        categoryDao.insertCategory(category)
        val categories = categoryDao.getAllCategories().first()

        // Assert
        assertEquals(1, categories.size)
        assertEquals("Entertainment", categories[0].type)
        assertEquals("entertainment_icon", categories[0].iconUrl)
    }

    @Test
    fun testMultipleCategoriesInserted() = runBlocking {
        // Arrange
        val categories = listOf(
            CategoryEntity(type = "Food", iconUrl = "food_icon"),
            CategoryEntity(type = "Transport", iconUrl = "transport_icon"),
            CategoryEntity(type = "Shopping", iconUrl = "shopping_icon")
        )

        // Act
        categories.forEach { categoryDao.insertCategory(it) }
        val result = categoryDao.getAllCategories().first()

        // Assert
        assertEquals(3, result.size)
    }

    @Test
    fun testCategoriesOrderedByTypeAscending() = runBlocking {
        // Arrange
        val categories = listOf(
            CategoryEntity(type = "Shopping", iconUrl = "shopping_icon"),
            CategoryEntity(type = "Food", iconUrl = "food_icon"),
            CategoryEntity(type = "Entertainment", iconUrl = "entertainment_icon")
        )

        // Act
        categories.forEach { categoryDao.insertCategory(it) }
        val result = categoryDao.getAllCategories().first()

        // Assert - Should be ordered alphabetically by type
        assertEquals("Entertainment", result[0].type)
        assertEquals("Food", result[1].type)
        assertEquals("Shopping", result[2].type)
    }

    @Test
    fun testCategoryWithSameTypeCanBeInserted() = runBlocking {
        // Arrange
        val category1 = CategoryEntity(type = "Food", iconUrl = "food_icon_1")
        val category2 = CategoryEntity(type = "Food", iconUrl = "food_icon_2")

        // Act
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)
        val categories = categoryDao.getAllCategories().first()

        // Assert - Room allows duplicates since id is auto-generated
        assertEquals(2, categories.size)
        assertTrue(categories.all { it.type == "Food" })
    }

    @Test
    fun testAutoGenerateIdWorks() = runBlocking {
        // Arrange & Act
        val category1 = CategoryEntity(type = "First", iconUrl = "first_icon")
        val category2 = CategoryEntity(type = "Second", iconUrl = "second_icon")

        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        val categories = categoryDao.getAllCategories().first()

        // Assert
        assertTrue(categories[0].id > 0)
        assertTrue(categories[1].id > 0)
        assertNotEquals(categories[0].id, categories[1].id)
    }
}