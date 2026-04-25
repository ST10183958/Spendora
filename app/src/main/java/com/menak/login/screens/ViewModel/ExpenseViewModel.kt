package com.menak.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.ExpenseEntity
import com.menak.login.data.Repository.ExpenseRepository
import com.menak.login.screens.State.ExpenseUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    private val _analyticsUiState = MutableStateFlow(AnalyticsUiState())
    val analyticsUiState: StateFlow<AnalyticsUiState> = _analyticsUiState.asStateFlow()

    private var filteredExpensesJob: Job? = null
    private var categoryTotalsJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                repository.getAllCategories(),
                repository.getAllExpenses(),
                repository.getBudgetGoal(),
                repository.getAllCategoryBudgetLimits()
            ) { categories, expenses, budgetGoal, categoryLimits ->

                val monthFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val calendar = Calendar.getInstance()
                val monthLabel = monthFormatter.format(calendar.time)

                val startCalendar = calendar.clone() as Calendar
                startCalendar.set(Calendar.DAY_OF_MONTH, 1)
                val monthStart = dateFormatter.format(startCalendar.time)

                val endCalendar = calendar.clone() as Calendar
                endCalendar.set(
                    Calendar.DAY_OF_MONTH,
                    endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                )
                val monthEnd = dateFormatter.format(endCalendar.time)

                val monthExpenses = expenses.filter { expense ->
                    expense.startDate >= monthStart && expense.startDate <= monthEnd
                }

                val monthlyBudget = budgetGoal?.monthlyTotalBudget ?: 0.0
                val monthlySpent = monthExpenses.sumOf { expense -> expense.amount }
                val monthlyRemaining = monthlyBudget - monthlySpent

                val dashboardCategoryItems = categories.map { category ->
                    val spent = monthExpenses
                        .filter { expense -> expense.categoryId == category.id }
                        .sumOf { expense -> expense.amount }

                    val limit = categoryLimits.firstOrNull { limitItem ->
                        limitItem.categoryId == category.id
                    }?.monthlyLimit

                    val remaining = limit?.minus(spent)

                    val progress = when {
                        limit != null && limit > 0.0 -> (spent / limit).toFloat().coerceIn(0f, 1f)
                        monthlyBudget > 0.0 -> (spent / monthlyBudget).toFloat().coerceIn(0f, 1f)
                        else -> 0f
                    }

                    CategoryDashboardItem(
                        categoryId = category.id,
                        categoryName = category.type,
                        spentAmount = spent,
                        remainingAmount = remaining,
                        progress = progress
                    )
                }

                _uiState.value.copy(
                    categories = categories,
                    expenses = expenses,
                    budgetGoal = budgetGoal,
                    categoryBudgetLimits = categoryLimits,
                    currentMonthLabel = monthLabel,
                    monthlyBudgetAmount = monthlyBudget,
                    monthlySpentAmount = monthlySpent,
                    monthlyRemainingAmount = monthlyRemaining,
                    dashboardCategorySpending = dashboardCategoryItems
                )
            }.collect { updatedState ->
                _uiState.value = updatedState
            }
        }

        viewModelScope.launch {
            combine(
                repository.getAllExpenses(),
                repository.getAllCategories()
            ) { expenses, categories ->
                buildAnalyticsUiState(expenses, categories)
            }.collect { analyticsState ->
                _analyticsUiState.value = analyticsState
            }
        }
    }

    fun onCategoryTypeChange(value: String) {
        _uiState.value = _uiState.value.copy(categoryType = value)
    }

    fun onCategoryIconUriChange(value: String) {
        _uiState.value = _uiState.value.copy(categoryIconUri = value)
    }

    fun onExpenseNameChange(value: String) {
        _uiState.value = _uiState.value.copy(expenseName = value)
    }

    fun onSelectedCategoryChange(categoryId: Int) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
    }

    fun onExpenseAmountChange(value: String) {
        _uiState.value = _uiState.value.copy(expenseAmount = value)
    }

    fun onExpenseStartDateChange(value: String) {
        _uiState.value = _uiState.value.copy(expenseStartDate = value)
    }

    fun onExpenseEndDateChange(value: String) {
        _uiState.value = _uiState.value.copy(expenseEndDate = value)
    }

    fun onExpenseDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(expenseDescription = value)
    }

    fun onExpenseIconUriChange(value: String) {
        _uiState.value = _uiState.value.copy(expenseIconUrl = value)
    }

    fun onReceiptPhotoUriChange(value: String) {
        _uiState.value = _uiState.value.copy(receiptPhotoUrl = value)
    }

    fun onMonthlyBudgetGoalInputChange(value: String) {
        _uiState.value = _uiState.value.copy(monthlyBudgetGoalInput = value)
    }

    fun onSelectedBudgetCategoryChange(categoryId: Int) {
        _uiState.value = _uiState.value.copy(selectedBudgetCategoryId = categoryId)
    }

    fun onCategoryBudgetLimitInputChange(value: String) {
        _uiState.value = _uiState.value.copy(categoryBudgetLimitInput = value)
    }

    fun onPeriodFromDateChange(value: String) {
        _uiState.value = _uiState.value.copy(periodFromDate = value)
    }

    fun onPeriodToDateChange(value: String) {
        _uiState.value = _uiState.value.copy(periodToDate = value)
    }

    fun setMessage(value: String) {
        _uiState.value = _uiState.value.copy(message = value)
    }

    fun addCategory() {
        val type = _uiState.value.categoryType.trim()
        val iconUri = _uiState.value.categoryIconUri.trim()

        if (type.isEmpty() || iconUri.isEmpty()) {
            setMessage("Enter category type and select an icon")
            return
        }

        viewModelScope.launch {
            repository.addCategory(type, iconUri)
            _uiState.value = _uiState.value.copy(
                categoryType = "",
                categoryIconUri = "",
                message = "Category added"
            )
        }
    }

    fun addExpense() {
        val name = _uiState.value.expenseName.trim()
        val categoryId = _uiState.value.selectedCategoryId
        val amount = _uiState.value.expenseAmount.toDoubleOrNull()
        val startDate = _uiState.value.expenseStartDate.trim()
        val endDate = _uiState.value.expenseEndDate.trim()
        val description = _uiState.value.expenseDescription.trim()
        val expenseIconUri = _uiState.value.expenseIconUrl.trim()
        val receiptPhotoUri = _uiState.value.receiptPhotoUrl.trim()

        if (
            name.isEmpty() ||
            categoryId == null ||
            amount == null ||
            startDate.isEmpty() ||
            endDate.isEmpty() ||
            description.isEmpty() ||
            expenseIconUri.isEmpty()
        ) {
            setMessage("Fill in all required expense fields correctly")
            return
        }

        viewModelScope.launch {
            repository.addExpense(
                name = name,
                categoryId = categoryId,
                amount = amount,
                startDate = startDate,
                endDate = endDate,
                description = description,
                expenseIconUri = expenseIconUri,
                receiptPhotoUrl = receiptPhotoUri
            )

            _uiState.value = _uiState.value.copy(
                expenseName = "",
                selectedCategoryId = null,
                expenseAmount = "",
                expenseStartDate = "",
                expenseEndDate = "",
                expenseDescription = "",
                expenseIconUrl = "",
                receiptPhotoUrl = "",
                message = "Expense added"
            )
        }
    }

    fun saveMonthlyBudgetGoal() {
        val amount = _uiState.value.monthlyBudgetGoalInput.toDoubleOrNull()
        if (amount == null) {
            setMessage("Enter a valid monthly budget goal")
            return
        }

        viewModelScope.launch {
            repository.saveMonthlyBudgetGoal(amount)
            _uiState.value = _uiState.value.copy(
                monthlyBudgetGoalInput = "",
                message = "Monthly budget goal saved"
            )
        }
    }

    fun saveCategoryBudgetLimit() {
        val categoryId = _uiState.value.selectedBudgetCategoryId
        val limit = _uiState.value.categoryBudgetLimitInput.toDoubleOrNull()

        if (categoryId == null || limit == null) {
            setMessage("Select a category and enter a valid limit")
            return
        }

        viewModelScope.launch {
            repository.saveCategoryBudgetLimit(categoryId, limit)
            _uiState.value = _uiState.value.copy(
                selectedBudgetCategoryId = null,
                categoryBudgetLimitInput = "",
                message = "Category budget limit saved"
            )
        }
    }

    fun loadExpensesForSelectedPeriod() {
        val from = _uiState.value.periodFromDate.trim()
        val to = _uiState.value.periodToDate.trim()

        if (from.isEmpty() || to.isEmpty()) {
            setMessage("Enter both from and to dates")
            return
        }

        filteredExpensesJob?.cancel()
        filteredExpensesJob = viewModelScope.launch {
            repository.getExpensesBetweenDates(from, to).collect { expenses ->
                _uiState.value = _uiState.value.copy(filteredExpenses = expenses)
            }
        }
    }

    fun loadCategoryTotalsForSelectedPeriod() {
        val from = _uiState.value.periodFromDate.trim()
        val to = _uiState.value.periodToDate.trim()

        if (from.isEmpty() || to.isEmpty()) {
            setMessage("Enter both from and to dates")
            return
        }

        categoryTotalsJob?.cancel()
        categoryTotalsJob = viewModelScope.launch {
            repository.getCategoryTotalsBetweenDates(from, to).collect { totals ->
                _uiState.value = _uiState.value.copy(categoryTotals = totals)
            }
        }
    }

    private fun buildAnalyticsUiState(
        expenses: List<ExpenseEntity>,
        categories: List<CategoryEntity>
    ): AnalyticsUiState {
        val monthFormatter = SimpleDateFormat("yyyy-MM", Locale.getDefault())

        val validExpenses = expenses.filter { expense ->
            expense.startDate.isNotBlank()
        }

        val totalSpent = validExpenses.sumOf { expense ->
            expense.amount
        }

        val groupedByDay = validExpenses
            .groupBy { expense -> expense.startDate }
            .mapValues { entry ->
                entry.value.sumOf { expense -> expense.amount }
            }
            .toSortedMap()

        val dailyAverage = if (groupedByDay.isNotEmpty()) {
            totalSpent / groupedByDay.size
        } else {
            0.0
        }

        val categoryNameMap = categories.associateBy { category ->
            category.id
        }

        val palette = listOf(
            0xFF00C853,
            0xFF2979FF,
            0xFFFF4081,
            0xFFFF5252,
            0xFFAA00FF,
            0xFFFF6D00,
            0xFF651FFF,
            0xFF00BFA5
        )

        val groupedByCategory = validExpenses.groupBy { expense ->
            expense.categoryId
        }

        val categoryBreakdown = groupedByCategory.entries.toList()
            .mapIndexed { index: Int, entry: Map.Entry<Int, List<ExpenseEntity>> ->
                val categoryName = categoryNameMap[entry.key]?.type ?: "Unknown"

                CategoryAnalyticsItem(
                    name = categoryName,
                    amount = entry.value.sumOf { expense -> expense.amount },
                    color = palette[index % palette.size]
                )
            }
            .sortedByDescending { item ->
                item.amount
            }

        val dailySpending = groupedByDay.entries.map { entry ->
            DailySpendingItem(
                date = entry.key,
                amount = entry.value
            )
        }

        val currentMonth = monthFormatter.format(Date())

        val previousMonthCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, -1)
        }
        val previousMonth = monthFormatter.format(previousMonthCalendar.time)

        val thisMonthTotal = validExpenses
            .filter { expense -> expense.startDate.startsWith(currentMonth) }
            .sumOf { expense -> expense.amount }

        val lastMonthTotal = validExpenses
            .filter { expense -> expense.startDate.startsWith(previousMonth) }
            .sumOf { expense -> expense.amount }

        return AnalyticsUiState(
            totalSpent = totalSpent,
            dailyAverage = dailyAverage,
            categoryBreakdown = categoryBreakdown,
            dailySpending = dailySpending,
            thisMonthTotal = thisMonthTotal,
            lastMonthTotal = lastMonthTotal
        )
    }
}

class ExpenseViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenseViewModel(repository) as T
    }
}