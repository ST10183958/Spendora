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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CategoryTotalsScreen(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Category Totals by Period",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.periodFromDate,
            onValueChange = viewModel::onPeriodFromDateChange,
            label = { Text("From Date") },
            placeholder = { Text("yyyy-MM-dd") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.periodToDate,
            onValueChange = viewModel::onPeriodToDateChange,
            label = { Text("To Date") },
            placeholder = { Text("yyyy-MM-dd") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.loadCategoryTotalsForSelectedPeriod() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Category Totals")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.categoryTotals) { total ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        if (total.categoryIconUrl.isNotEmpty()) {
                            AndroidView(
                                modifier = Modifier.size(48.dp),
                                factory = { context ->
                                    ImageView(context).apply {
                                        layoutParams = android.view.ViewGroup.LayoutParams(120, 120)
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                    }
                                },
                                update = { imageView ->
                                    try {
                                        imageView.setImageURI(Uri.parse(total.categoryIconUrl))
                                    } catch (_: Exception) {
                                        imageView.setImageDrawable(null)
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Column {
                            Text("Category: ${total.categoryType}")
                            Text("Total Spent: R %.2f".format(total.totalSpent))
                        }
                    }
                }
            }
        }
    }
}