package com.menak.login.ui

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun ExpenseHomeScreen(
    navController: NavController,
    viewModel: ExpenseViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Expense Manager",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("add_category") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Category")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("add_expense") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Saved Expenses",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.expenses) { expense ->
                val category = uiState.categories.firstOrNull { it.id == expense.categoryId }
                val categoryName = category?.type ?: "Unknown"
                val categoryIconUri = category?.iconUrl.orEmpty()

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (expense.expenseIconUrl.isNotEmpty()) {
                                AndroidView(
                                    modifier = Modifier.size(56.dp),
                                    factory = { context ->
                                        ImageView(context).apply {
                                            layoutParams = android.view.ViewGroup.LayoutParams(140, 140)
                                            scaleType = ImageView.ScaleType.CENTER_CROP
                                        }
                                    },
                                    update = { imageView ->
                                        try {
                                            imageView.setImageURI(Uri.parse(expense.expenseIconUrl))
                                        } catch (_: Exception) {
                                            imageView.setImageDrawable(null)
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.size(12.dp))
                            }

                            if (categoryIconUri.isNotEmpty()) {
                                AndroidView(
                                    modifier = Modifier.size(40.dp),
                                    factory = { context ->
                                        ImageView(context).apply {
                                            layoutParams = android.view.ViewGroup.LayoutParams(100, 100)
                                            scaleType = ImageView.ScaleType.CENTER_CROP
                                        }
                                    },
                                    update = { imageView ->
                                        try {
                                            imageView.setImageURI(Uri.parse(categoryIconUri))
                                        } catch (_: Exception) {
                                            imageView.setImageDrawable(null)
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.size(12.dp))
                            }

                            Column {
                                Text("Expense: ${expense.expenseName}")
                                Text("Category: $categoryName")
                                Text("Amount: R %.2f".format(expense.amount))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Start Date: ${expense.startDate}")
                        Text("End Date: ${expense.endDate}")
                        Text("Description: ${expense.description}")
                    }
                }
            }
        }
    }
}