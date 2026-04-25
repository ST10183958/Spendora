package com.menak.login.ui

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.menak.login.ui.components.DatePickerField

@Composable
fun ExpensePeriodListScreen(
    viewModel: ExpenseViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFB))
            .consumeWindowInsets(WindowInsets.safeDrawing)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    color = Color(0xFF00BFA5),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Expense History",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Review expenses by period",
                        color = Color(0xFFE0F2F1),
                        fontSize = 14.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .offset(y = (-8).dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                HistoryInputCard(
                    title = "Filter Period",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    DatePickerField(
                        label = "From Date",
                        value = uiState.periodFromDate,
                        onDateSelected = viewModel::onPeriodFromDateChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DatePickerField(
                        label = "To Date",
                        value = uiState.periodToDate,
                        onDateSelected = viewModel::onPeriodToDateChange
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.loadExpensesForSelectedPeriod() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BFA5)
                        )
                    ) {
                        Text("Load Expenses", color = Color.White)
                    }
                }

                if (uiState.message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.filteredExpenses.isEmpty()) {
                    HistoryInputCard(
                        title = "No History Yet",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Color(0xFF00897B)
                            )
                        }
                    ) {
                        Text(
                            text = "Select a period to view saved expenses.",
                            color = Color.Gray
                        )
                    }
                } else {
                    uiState.filteredExpenses.forEach { expense ->
                        val category = uiState.categories.firstOrNull { it.id == expense.categoryId }
                        val categoryName = category?.type ?: "Unknown"

                        HistoryExpenseCard(
                            expenseName = expense.expenseName,
                            categoryName = categoryName,
                            amount = expense.amount,
                            startDate = expense.startDate,
                            endDate = expense.endDate,
                            description = expense.description,
                            receiptPhotoUri = expense.receiptPhotoUrl
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun HistoryInputCard(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color(0xFF00897B),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

@Composable
private fun HistoryExpenseCard(
    expenseName: String,
    categoryName: String,
    amount: Double,
    startDate: String,
    endDate: String,
    description: String,
    receiptPhotoUri: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = expenseName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF1A1A2E)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Category: $categoryName",
                        color = Color(0xFF00897B)
                    )
                }

                Text(
                    text = "R %.2f".format(amount),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BFA5),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF00BFA5), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Date: $startDate", color = Color.DarkGray, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF2979FF), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("End Date: $endDate", color = Color.DarkGray, fontSize = 13.sp)
            }

            if (description.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Description",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A2E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = Color.Gray
                )
            }

            if (receiptPhotoUri.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF00897B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Receipt",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF00897B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    },
                    update = { imageView ->
                        try {
                            imageView.setImageURI(Uri.parse(receiptPhotoUri))
                        } catch (_: Exception) {
                            imageView.setImageDrawable(null)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun historyTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color(0xFFF1F3F4),
    unfocusedContainerColor = Color(0xFFF1F3F4),
    disabledContainerColor = Color(0xFFF1F3F4),
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
)