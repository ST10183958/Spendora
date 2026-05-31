package com.menak.login.data.Dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.menak.login.data.AppDatabase
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        expenseDao = database.expenseDao()
        categoryDao = database.categoryDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun testExpenseInsertedCorrectly() = runBlocking {
        // Arrange - Create and insert category first (foreign key requirement)
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon_url")
        categoryDao.insertCategory(category)

        val expense = ExpenseEntity(
            expenseName = "Groceries",
            categoryId = 1,
            amount = 150.50,
            startDate = "2026-05-01",
            endDate = "2026-05-01",
            description = "Weekly grocery shopping",
            expenseIconUrl = "grocery_icon",
            receiptPhotoUrl = "receipt_url"
        )

        // Act
        expenseDao.insertExpense(expense)
        val expenses = expenseDao.getAllExpenses().first()

        // Assert
        assertNotNull(expenses)
        assertEquals(1, expenses.size)
        assertEquals("Groceries", expenses[0].expenseName)
        assertEquals(150.50, expenses[0].amount, 0.01)
        assertEquals("2026-05-01", expenses[0].startDate)
    }

    @Test
    fun testMultipleExpensesInserted() = runBlocking {
        // Arrange
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon")
        categoryDao.insertCategory(category)

        val expense1 = ExpenseEntity(
            expenseName = "Groceries",
            categoryId = 1,
            amount = 100.0,
            startDate = "2026-05-01",
            endDate = "2026-05-01",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        val expense2 = ExpenseEntity(
            expenseName = "Restaurant",
            categoryId = 1,
            amount = 75.50,
            startDate = "2026-05-02",
            endDate = "2026-05-02",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        // Act
        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)
        val expenses = expenseDao.getAllExpenses().first()

        // Assert
        assertEquals(2, expenses.size)
    }

    @Test
    fun testGetExpensesBetweenDates() = runBlocking {
        // Arrange
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon")
        categoryDao.insertCategory(category)

        val expense1 = ExpenseEntity(
            expenseName = "Early Expense",
            categoryId = 1,
            amount = 50.0,
            startDate = "2026-05-01",
            endDate = "2026-05-01",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        val expense2 = ExpenseEntity(
            expenseName = "Mid Expense",
            categoryId = 1,
            amount = 100.0,
            startDate = "2026-05-15",
            endDate = "2026-05-15",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        val expense3 = ExpenseEntity(
            expenseName = "Late Expense",
            categoryId = 1,
            amount = 75.0,
            startDate = "2026-06-01",
            endDate = "2026-06-01",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)
        expenseDao.insertExpense(expense3)

        // Act
        val filteredExpenses = expenseDao.getExpensesBetweenDates("2026-05-01", "2026-05-31").first()

        // Assert
        assertEquals(2, filteredExpenses.size)
        assertTrue(filteredExpenses.any { it.expenseName == "Early Expense" })
        assertTrue(filteredExpenses.any { it.expenseName == "Mid Expense" })
        assertFalse(filteredExpenses.any { it.expenseName == "Late Expense" })
    }

    @Test
    fun testGetCategoryTotalsBetweenDates() = runBlocking {
        // Arrange
        val category1 = CategoryEntity(type = "Food", iconUrl = "food_icon")
        val category2 = CategoryEntity(type = "Transport", iconUrl = "transport_icon")
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        val foodExpense1 = ExpenseEntity(
            expenseName = "Groceries",
            categoryId = 1,
            amount = 100.0,
            startDate = "2026-05-10",
            endDate = "2026-05-10",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        val foodExpense2 = ExpenseEntity(
            expenseName = "Restaurant",
            categoryId = 1,
            amount = 50.0,
            startDate = "2026-05-15",
            endDate = "2026-05-15",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        val transportExpense = ExpenseEntity(
            expenseName = "Bus Ticket",
            categoryId = 2,
            amount = 30.0,
            startDate = "2026-05-12",
            endDate = "2026-05-12",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        expenseDao.insertExpense(foodExpense1)
        expenseDao.insertExpense(foodExpense2)
        expenseDao.insertExpense(transportExpense)

        // Act
        val categoryTotals = expenseDao.getCategoryTotalsBetweenDates("2026-05-01", "2026-05-31").first()

        // Assert
        assertEquals(2, categoryTotals.size)

        val foodTotal = categoryTotals.firstOrNull { it.categoryId == 1 }
        assertNotNull(foodTotal)
        foodTotal?.totalSpent?.let { assertEquals(150.0, it, 0.01) }

        val transportTotal = categoryTotals.firstOrNull { it.categoryId == 2 }
        assertNotNull(transportTotal)
        transportTotal?.totalSpent?.let { assertEquals(30.0, it, 0.01) }
    }

    @Test
    fun testGetCategoryTotalsWithNoExpenses() = runBlocking {
        // Arrange
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon")
        categoryDao.insertCategory(category)

        // Act
        val categoryTotals = expenseDao.getCategoryTotalsBetweenDates("2026-05-01", "2026-05-31").first()

        // Assert - Should still return category with zero spent
        assertTrue(categoryTotals.isNotEmpty())
        assertEquals(1, categoryTotals.size)
        assertEquals(0.0, categoryTotals[0].totalSpent, 0.01)
    }

    @Test
    fun testExpensesOrderedByDescendingId() = runBlocking {
        // Arrange
        val category = CategoryEntity(type = "Food", iconUrl = "food_icon")
        categoryDao.insertCategory(category)

        val expense1 = ExpenseEntity(
            expenseName = "First",
            categoryId = 1,
            amount = 10.0,
            startDate = "2026-05-01",
            endDate = "2026-05-01",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        val expense2 = ExpenseEntity(
            expenseName = "Second",
            categoryId = 1,
            amount = 20.0,
            startDate = "2026-05-02",
            endDate = "2026-05-02",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        // Act
        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)
        val expenses = expenseDao.getAllExpenses().first()

        // Assert - Most recent (higher ID) should come first due to DESC
        assertEquals(2, expenses.size)
        assertTrue(expenses[0].id > expenses[1].id)
    }
}