package com.menak.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.menak.login.data.Repository.ExpenseRepository
import com.menak.login.screens.State.ExpenseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

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
                _uiState.value.copy(
                    categories = categories,
                    expenses = expenses,
                    budgetGoal = budgetGoal,
                    categoryBudgetLimits = categoryLimits
                )
            }.collect { updatedState ->
                _uiState.value = updatedState
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
        val expenseIconUrl = _uiState.value.expenseIconUrl.trim()
        val receiptPhotoUrl = _uiState.value.receiptPhotoUrl.trim()

        if (
            name.isEmpty() ||
            categoryId == null ||
            amount == null ||
            startDate.isEmpty() ||
            endDate.isEmpty() ||
            description.isEmpty() ||
            expenseIconUrl.isEmpty()
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
                expenseIconUri = expenseIconUrl,
                receiptPhotoUrl = receiptPhotoUrl
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
}

class ExpenseViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenseViewModel(repository) as T
    }
}