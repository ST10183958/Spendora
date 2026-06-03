package com.menak.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.menak.login.data.Repository.ExpenseRepository
import com.menak.login.data.Entity.ExpenseEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ---------------- UI EVENTS ----------------

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    // ---------------- UI STATE ----------------

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState

    private val _analyticsUiState = MutableStateFlow(AnalyticsUiState())
    val analyticsUiState: StateFlow<AnalyticsUiState> = _analyticsUiState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // ---------------- SNACKBAR ----------------

    fun sendSnackbar(message: String) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.ShowSnackbar(message))
        }
    }

    // =========================================================
    // EXPENSE INPUTS
    // =========================================================

    fun onExpenseNameChange(value: String) {
        _uiState.update { it.copy(expenseName = value) }
    }

    fun onExpenseAmountChange(value: String) {
        _uiState.update { it.copy(expenseAmount = value) }
    }

    fun onExpenseStartDateChange(value: String) {
        _uiState.update { it.copy(expenseStartDate = value) }
    }

    fun onExpenseEndDateChange(value: String) {
        _uiState.update { it.copy(expenseEndDate = value) }
    }

    fun onSelectedCategoryChange(id: Int) {
        _uiState.update { it.copy(selectedCategoryId = id) }
    }

    fun onExpenseDescriptionChange(value: String) {
        _uiState.update { it.copy(expenseDescription = value) }
    }

    fun onExpenseIconUriChange(uri: String) {
        _uiState.update { it.copy(expenseIconUrl = uri) }
    }

    fun onReceiptPhotoUriChange(uri: String) {
        _uiState.update { it.copy(receiptPhotoUrl = uri) }
    }

    // =========================================================
    // ADD EXPENSE (FIXED)
    // =========================================================

    fun addExpense() {
        val state = _uiState.value

        val amount = state.expenseAmount.toDoubleOrNull()

        if (state.expenseName.isBlank() || amount == null) {
            sendSnackbar("Please fill required fields")
            return
        }

        val categoryId = state.selectedCategoryId
        if (categoryId == null) {
            sendSnackbar("Please select category")
            return
        }

        viewModelScope.launch {
            try {
                repository.addExpense(
                    ExpenseEntity(
                        name = state.expenseName,
                        categoryId = categoryId,
                        amount = amount,
                        startDate = state.expenseStartDate,
                        endDate = state.expenseEndDate,
                        description = state.expenseDescription,
                        expenseIconUrl = state.expenseIconUrl,
                        receiptPhotoUrl = state.receiptPhotoUrl
                    )
                )

                sendSnackbar("Expense added successfully")

                _uiState.update {
                    it.copy(
                        expenseName = "",
                        expenseAmount = "",
                        expenseStartDate = "",
                        expenseEndDate = "",
                        expenseDescription = "",
                        expenseIconUrl = "",
                        receiptPhotoUrl = ""
                    )
                }

            } catch (e: Exception) {
                sendSnackbar("Error: ${e.message}")
            }
        }
    }

    // =========================================================
    // CATEGORY
    // =========================================================

    fun onCategoryTypeChange(value: String) {
        _uiState.update { it.copy(categoryType = value) }
    }

    fun onCategoryIconUriChange(uri: String) {
        _uiState.update { it.copy(categoryIconUri = uri) }
    }

    fun setMessage(message: String) {
        _uiState.update { it.copy(message = message) }
    }

    fun addCategory() {
        val state = _uiState.value

        if (state.categoryType.isBlank()) {
            setMessage("Category name required")
            return
        }

        viewModelScope.launch {
            try {
                repository.addCategory(
                    type = state.categoryType,
                    iconUrl = state.categoryIconUri
                )

                setMessage("Category added")

                _uiState.update {
                    it.copy(
                        categoryType = "",
                        categoryIconUri = ""
                    )
                }

            } catch (e: Exception) {
                setMessage("Error: ${e.message}")
            }
        }
    }

    // =========================================================
    // BUDGET
    // =========================================================

    fun onMonthlyBudgetGoalInputChange(value: String) {
        _uiState.update { it.copy(monthlyBudgetGoalInput = value) }
    }

    fun onSelectedBudgetCategoryChange(categoryId: Int) {
        _uiState.update { it.copy(selectedBudgetCategoryId = categoryId) }
    }

    fun onCategoryBudgetLimitInputChange(value: String) {
        _uiState.update { it.copy(categoryBudgetLimitInput = value) }
    }

    fun saveMonthlyBudgetGoal() {
        val amount = _uiState.value.monthlyBudgetGoalInput.toDoubleOrNull() ?: return

        viewModelScope.launch {
            repository.saveMonthlyBudgetGoal(amount)

            _uiState.update {
                it.copy(
                    message = "Monthly budget saved",
                    monthlyBudgetGoalInput = ""
                )
            }
        }
    }

    fun saveCategoryBudgetLimit() {
        val state = _uiState.value

        val categoryId = state.selectedBudgetCategoryId ?: return
        val limit = state.categoryBudgetLimitInput.toDoubleOrNull() ?: return

        viewModelScope.launch {
            repository.saveCategoryBudgetLimit(categoryId, limit)

            _uiState.update {
                it.copy(
                    message = "Category limit saved",
                    categoryBudgetLimitInput = ""
                )
            }
        }
    }

    fun onPeriodFromDateChange(value: String) {
        _uiState.update {
            it.copy(periodFromDate = value)
        }
    }

    fun onPeriodToDateChange(value: String) {
        _uiState.update {
            it.copy(periodToDate = value)
        }
    }

    fun loadCategoryTotalsForSelectedPeriod() {
        val from = _uiState.value.periodFromDate
        val to = _uiState.value.periodToDate

        if (from.isBlank() || to.isBlank()) {
            sendSnackbar("Please select both dates")
            return
        }

        viewModelScope.launch {
            try {
                val totals = repository.getCategoryTotalsBetweenDates(from, to)
                    .first() // convert Flow → List

                _uiState.update {
                    it.copy(categoryTotals = totals)
                }

            } catch (e: Exception) {
                sendSnackbar("Failed to load totals: ${e.message}")
            }
        }
    }

    fun loadExpensesForSelectedPeriod() {
        val from = _uiState.value.periodFromDate
        val to = _uiState.value.periodToDate

        if (from.isBlank() || to.isBlank()) {
            sendSnackbar("Please select both dates")
            return
        }

        viewModelScope.launch {
            try {
                val expenses = repository.getExpensesBetweenDates(from, to)
                    .first()

                _uiState.update {
                    it.copy(filteredExpenses = expenses)
                }

            } catch (e: Exception) {
                sendSnackbar("Failed to load expenses: ${e.message}")
            }
        }
    }
}