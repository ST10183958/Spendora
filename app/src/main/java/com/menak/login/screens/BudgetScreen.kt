package com.menak.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val selectedCategoryName = uiState.categories
        .firstOrNull { it.id == uiState.selectedBudgetCategoryId }
        ?.type ?: "Select Category"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Budget Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Monthly Total Budget Goal")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.monthlyBudgetGoalInput,
                    onValueChange = viewModel::onMonthlyBudgetGoalInputChange,
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.saveMonthlyBudgetGoal() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Monthly Goal")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Current Goal: ${
                        uiState.budgetGoal?.monthlyTotalBudget?.let { "R %.2f".format(it) } ?: "Not set"
                    }"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Category Budget Limit")

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        uiState.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.type) },
                                onClick = {
                                    viewModel.onSelectedBudgetCategoryChange(category.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.categoryBudgetLimitInput,
                    onValueChange = viewModel::onCategoryBudgetLimitInputChange,
                    label = { Text("Limit Amount") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.saveCategoryBudgetLimit() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Category Limit")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Saved Category Limits",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.categoryBudgetLimits) { limit ->
                val categoryName = uiState.categories.firstOrNull { it.id == limit.categoryId }?.type ?: "Unknown"

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Category: $categoryName")
                        Text("Monthly Limit: R %.2f".format(limit.monthlyLimit))
                    }
                }
            }
        }
    }
}