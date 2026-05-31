package com.menak.login.data.Dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.menak.login.data.AppDatabase
import com.menak.login.data.Entity.BudgetGoalEntity
import com.menak.login.data.Entity.CategoryBudgetLimitEntity
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
class BudgetDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var budgetDao: BudgetDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        budgetDao = database.budgetDao()
        categoryDao = database.categoryDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun testBudgetGoalSavedCorrectly() = runBlocking {
        // Arrange
        val budgetGoal = BudgetGoalEntity(id = 1, monthlyTotalBudget = 5000.0)

        // Act
        budgetDao.upsertBudgetGoal(budgetGoal)
        val result = budgetDao.getBudgetGoal().first()

        // Assert
        assertNotNull(result)
        result?.monthlyTotalBudget?.let { assertEquals(5000.0, it, 0.01) }
        assertEquals(1, result?.id)
    }

    @Test
    fun testBudgetGoalUpdatedOnConflict() = runBlocking {
        // Arrange
        val initialGoal = BudgetGoalEntity(id = 1, monthlyTotalBudget = 3000.0)
        val updatedGoal = BudgetGoalEntity(id = 1, monthlyTotalBudget = 4500.0)

        // Act
        budgetDao.upsertBudgetGoal(initialGoal)
        budgetDao.upsertBudgetGoal(updatedGoal)
        val result = budgetDao.getBudgetGoal().first()

        // Assert - Should be replaced with new value
        assertNotNull(result)
        result?.monthlyTotalBudget?.let { assertEquals(4500.0, it, 0.01) }
    }

    @Test
    fun testCategoryBudgetLimitSavedCorrectly() = runBlocking {
        // Arrange - Need category for foreign key
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon")
        categoryDao.insertCategory(category)

        val budgetLimit = CategoryBudgetLimitEntity(
            id = 1,
            categoryId = 1,
            monthlyLimit = 1000.0
        )

        // Act
        budgetDao.upsertCategoryBudgetLimit(budgetLimit)
        val limits = budgetDao.getAllCategoryBudgetLimits().first()

        // Assert
        assertEquals(1, limits.size)
        assertEquals(1, limits[0].categoryId)
        assertEquals(1000.0, limits[0].monthlyLimit, 0.01)
    }

    @Test
    fun testMultipleCategoryBudgetLimits() = runBlocking {
        // Arrange
        val category1 = CategoryEntity(type = "Food", iconUrl = "food_icon")
        val category2 = CategoryEntity(type = "Transport", iconUrl = "transport_icon")
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        val limit1 = CategoryBudgetLimitEntity(
            id = 1,
            categoryId = 1,
            monthlyLimit = 1000.0
        )

        val limit2 = CategoryBudgetLimitEntity(
            id = 2,
            categoryId = 2,
            monthlyLimit = 500.0
        )

        // Act
        budgetDao.upsertCategoryBudgetLimit(limit1)
        budgetDao.upsertCategoryBudgetLimit(limit2)
        val limits = budgetDao.getAllCategoryBudgetLimits().first()

        // Assert
        assertEquals(2, limits.size)
        assertTrue(limits.any { it.categoryId == 1 && it.monthlyLimit == 1000.0 })
        assertTrue(limits.any { it.categoryId == 2 && it.monthlyLimit == 500.0 })
    }

    @Test
    fun testCategoryBudgetLimitUpdatedOnConflict() = runBlocking {
        // Arrange
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon")
        categoryDao.insertCategory(category)

        val initialLimit = CategoryBudgetLimitEntity(
            id = 1,
            categoryId = 1,
            monthlyLimit = 800.0
        )

        val updatedLimit = CategoryBudgetLimitEntity(
            id = 1,
            categoryId = 1,
            monthlyLimit = 1200.0
        )

        // Act
        budgetDao.upsertCategoryBudgetLimit(initialLimit)
        budgetDao.upsertCategoryBudgetLimit(updatedLimit)
        val limits = budgetDao.getAllCategoryBudgetLimits().first()

        // Assert
        assertEquals(1, limits.size)
        assertEquals(1200.0, limits[0].monthlyLimit, 0.01)
    }

    @Test
    fun testBudgetGoalReturnsNullWhenNotSet() = runBlocking {
        // Act
        val result = budgetDao.getBudgetGoal().first()

        // Assert
        assertNull(result)
    }

    @Test
    fun testCategoryBudgetLimitsEmptyWhenNoLimits() = runBlocking {
        // Act
        val limits = budgetDao.getAllCategoryBudgetLimits().first()

        // Assert
        assertTrue(limits.isEmpty())
    }

    @Test
    fun testCategoryBudgetLimitOrderedById() = runBlocking {
        // Arrange
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon")
        categoryDao.insertCategory(category)

        val limit2 = CategoryBudgetLimitEntity(
            id = 2,
            categoryId = 1,
            monthlyLimit = 500.0
        )

        val limit1 = CategoryBudgetLimitEntity(
            id = 1,
            categoryId = 1,
            monthlyLimit = 1000.0
        )

        // Act
        budgetDao.upsertCategoryBudgetLimit(limit2)
        budgetDao.upsertCategoryBudgetLimit(limit1)
        val limits = budgetDao.getAllCategoryBudgetLimits().first()

        // Assert - Should be ordered by id ASC
        assertEquals(2, limits.size)
        assertEquals(1, limits[0].id)
        assertEquals(2, limits[1].id)
    }
}