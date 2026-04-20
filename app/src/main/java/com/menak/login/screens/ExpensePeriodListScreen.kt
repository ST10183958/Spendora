package com.menak.login.ui

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.menak.login.ui.components.DatePickerField

@Composable
fun ExpensePeriodListScreen(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Expenses by Period",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        DatePickerField(
            label = "From Date",
            value = uiState.periodFromDate,
            onDateSelected = viewModel::onPeriodFromDateChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(
            label = "To Date",
            value = uiState.periodToDate,
            onDateSelected = viewModel::onPeriodToDateChange
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.loadExpensesForSelectedPeriod() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Expenses")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.filteredExpenses) { expense ->
                val category = uiState.categories.firstOrNull { it.id == expense.categoryId }
                val categoryName = category?.type ?: "Unknown"

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Expense: ${expense.expenseName}")
                        Text("Category: $categoryName")
                        Text("Amount: R %.2f".format(expense.amount))
                        Text("Start Date: ${expense.startDate}")
                        Text("End Date: ${expense.endDate}")
                        Text("Description: ${expense.description}")

                        if (expense.receiptPhotoUrl.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Receipt Photo")

                            Spacer(modifier = Modifier.height(8.dp))

                            AndroidView(
                                modifier = Modifier.size(140.dp),
                                factory = { context ->
                                    ImageView(context).apply {
                                        layoutParams = android.view.ViewGroup.LayoutParams(300, 300)
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                    }
                                },
                                update = { imageView ->
                                    try {
                                        imageView.setImageURI(Uri.parse(expense.receiptPhotoUrl))
                                    } catch (_: Exception) {
                                        imageView.setImageDrawable(null)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}