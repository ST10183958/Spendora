package com.menak.login

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.menak.login.data.AppDatabase
import com.menak.login.data.Dao.BudgetDao
import com.menak.login.data.Dao.CategoryDao
import com.menak.login.data.Dao.ExpenseDao
import com.menak.login.data.Entity.BudgetGoalEntity
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var budgetDao: BudgetDao

    private val testCategoryId = 1

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        expenseDao = db.expenseDao()
        categoryDao = db.categoryDao()
        budgetDao = db.budgetDao()

        // ✅ IMPORTANT: always insert FK parent data first
        categoryDao.insertCategory(
            CategoryEntity(
                id = testCategoryId,
                type = "Food",
                iconUrl = ""
            )
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    // -----------------------------
    // 1) insertExpense()
    // -----------------------------
    @Test
    fun insertExpense_shouldSaveExpense() = runTest {

        val expense = ExpenseEntity(
            id = 1,
            expenseName = "Food",
            amount = 100.0,
            categoryId = testCategoryId,
            startDate = "2026-01-01",
            endDate = "2026-01-01",
            description = "",
            expenseIconUrl = "",
            receiptPhotoUrl = ""
        )

        expenseDao.insertExpense(expense)

        val result = expenseDao.getAllExpenses().first()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Food", result[0].expenseName)
    }

    // -----------------------------
    // 2) getAllExpenses()
    // -----------------------------
    @Test
    fun getAllExpenses_shouldReturnAllExpenses() = runTest {

        expenseDao.insertExpense(
            ExpenseEntity(
                id = 1,
                expenseName = "Food",
                amount = 100.0,
                categoryId = testCategoryId,
                startDate = "2026-01-01",
                endDate = "2026-01-01",
                description = "",
                expenseIconUrl = "",
                receiptPhotoUrl = ""
            )
        )

        expenseDao.insertExpense(
            ExpenseEntity(
                id = 2,
                expenseName = "Transport",
                amount = 50.0,
                categoryId = testCategoryId,
                startDate = "2026-01-01",
                endDate = "2026-01-01",
                description = "",
                expenseIconUrl = "",
                receiptPhotoUrl = ""
            )
        )

        val result = expenseDao.getAllExpenses().first()

        Assert.assertEquals(2, result.size)
    }

    // -----------------------------
    // 3) getExpensesBetweenDates()
    // -----------------------------
    @Test
    fun getExpensesBetweenDates_shouldReturnCorrectRange() = runTest {

        expenseDao.insertExpense(
            ExpenseEntity(
                id = 1,
                expenseName = "Food",
                amount = 100.0,
                categoryId = testCategoryId,
                startDate = "2026-01-01",
                endDate = "2026-01-01",
                description = "",
                expenseIconUrl = "",
                receiptPhotoUrl = ""
            )
        )

        expenseDao.insertExpense(
            ExpenseEntity(
                id = 2,
                expenseName = "Transport",
                amount = 50.0,
                categoryId = testCategoryId,
                startDate = "2026-02-10",
                endDate = "2026-02-10",
                description = "",
                expenseIconUrl = "",
                receiptPhotoUrl = ""
            )
        )

        val result =
            expenseDao.getExpensesBetweenDates("2026-01-01", "2026-01-31").first()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Food", result[0].expenseName)
    }

    // -----------------------------
    // 4) getCategoryTotals()
    // -----------------------------
    @Test
    fun getCategoryTotals_shouldCalculateTotals() = runTest {

        expenseDao.insertExpense(
            ExpenseEntity(
                id = 1,
                expenseName = "Food",
                amount = 100.0,
                categoryId = testCategoryId,
                startDate = "2026-01-01",
                endDate = "2026-01-01",
                description = "",
                expenseIconUrl = "",
                receiptPhotoUrl = ""
            )
        )

        expenseDao.insertExpense(
            ExpenseEntity(
                id = 2,
                expenseName = "Food",
                amount = 50.0,
                categoryId = testCategoryId,
                startDate = "2026-01-01",
                endDate = "2026-01-01",
                description = "",
                expenseIconUrl = "",
                receiptPhotoUrl = ""
            )
        )

        val result = expenseDao.getCategoryTotalsBetweenDates(
            fromDate = "2026-01-01",
            toDate = "2026-01-31"
        ).first()

        val foodTotal = result.first { it.categoryId == testCategoryId }.totalSpent

        Assert.assertEquals(150.0, foodTotal, 0.01)
    }

    // -----------------------------
    // 5) insertCategory()
    // -----------------------------
    @Test
    fun insertCategory_shouldSaveCategory() = runTest {

        val result = categoryDao.getAllCategories().first()

        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals("Food", result[0].type)
    }

    @Test
    fun getAllCategories_shouldReturnCategories() = runTest {

        val result = categoryDao.getAllCategories().first()

        Assert.assertEquals(1, result.size)
    }

    // -----------------------------
    // 6) Budget tests
    // -----------------------------
    @Test
    fun upsertBudgetGoal_shouldSaveBudget() = runTest {

        val goal = BudgetGoalEntity(
            id = 1,
            monthlyTotalBudget = 5000.0
        )

        budgetDao.upsertBudgetGoal(goal)

        val result = budgetDao.getBudgetGoal().first()

        Assert.assertNotNull(result)
        Assert.assertEquals(5000.0, result?.monthlyTotalBudget)
    }

    @Test
    fun getBudgetGoal_shouldReturnBudgetGoal() = runTest {

        budgetDao.upsertBudgetGoal(
            BudgetGoalEntity(id = 1, monthlyTotalBudget = 3000.0)
        )

        val result = budgetDao.getBudgetGoal().first()

        Assert.assertEquals(3000.0, result?.monthlyTotalBudget)
    }
}