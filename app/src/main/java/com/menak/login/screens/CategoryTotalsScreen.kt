package com.menak.login.ui

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.menak.login.ui.components.DatePickerField

@Composable
fun CategoryTotalsScreen(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Map categories for lookup (IMPORTANT FIX)
    val categoryMap = uiState.categories.associateBy { it.id }

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
            onClick = { viewModel.loadCategoryTotalsForSelectedPeriod() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Category Totals")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.categoryTotals) { total ->

                val category = categoryMap[total.categoryId]

                Card(modifier = Modifier.fillMaxWidth()) {

                    Row(modifier = Modifier.padding(12.dp)) {

                        // ICON
                        if (!category?.iconUrl.isNullOrEmpty()) {
                            AndroidView(
                                modifier = Modifier.size(48.dp),
                                factory = { context ->
                                    ImageView(context).apply {
                                        layoutParams =
                                            android.view.ViewGroup.LayoutParams(120, 120)
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                    }
                                },
                                update = { imageView ->
                                    try {
                                        imageView.setImageURI(
                                            Uri.parse(category?.iconUrl)
                                        )
                                    } catch (_: Exception) {
                                        imageView.setImageDrawable(null)
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        // TEXT
                        Column {
                            Text(
                                text = "Category: ${category?.type ?: "Unknown"}"
                            )

                            Text(
                                text = "Total Spent: R %.2f".format(total.total)
                            )
                        }
                    }
                }
            }
        }
    }
}