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

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAllCategories(),
                repository.getAllExpenses()
            ) { categories, expenses ->
                _uiState.value.copy(
                    categories = categories,
                    expenses = expenses
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

    fun onExpenseIconUrlChange(value: String) {
        _uiState.value = _uiState.value.copy(expenseIconIrl = value)
    }

    fun addCategory() {
        val type = _uiState.value.categoryType.trim()
        val iconUri = _uiState.value.categoryIconUri.trim()

        if (type.isEmpty() || iconUri.isEmpty()) {
            _uiState.value = _uiState.value.copy(message = "Enter category type and select an icon")
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
        val iconUrl = _uiState.value.expenseIconIrl.trim()

        if (
            name.isEmpty() ||
            categoryId == null ||
            amount == null ||
            startDate.isEmpty() ||
            endDate.isEmpty() ||
            description.isEmpty() ||
            iconUrl.isEmpty()
            ) {
            _uiState.value = _uiState.value.copy(message = "Fill in all expense fields correctly")
            return
        }

        viewModelScope.launch {
            repository.addExpense(name, categoryId, amount, startDate,endDate,description, expenseIconUrl = iconUrl)
            _uiState.value = _uiState.value.copy(
                expenseName = "",
                selectedCategoryId = null,
                expenseAmount = "",
                expenseStartDate = "",
                expenseEndDate = "",
                expenseDescription = "",
                expenseIconIrl = "",
                message = "Expense added"
            )
        }
    }
    fun setMessage(value: String) {
        _uiState.value = _uiState.value.copy(message = value)
    }
}

class ExpenseViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenseViewModel(repository) as T
    }
}