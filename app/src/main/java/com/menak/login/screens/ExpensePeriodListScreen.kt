package com.menak.login.screens

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.menak.login.ui.ExpenseViewModel
import com.menak.login.ui.components.DatePickerField
import com.menak.login.screens.ViewModel.CurrencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensePeriodListScreen(
    viewModel: ExpenseViewModel,
    onBackClick: () -> Unit,
    currencyVM: CurrencyViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val currency = currencyVM.currency.value

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }

    val displayedExpenses =
        if (selectedCategory == "All") {
            uiState.filteredExpenses
        } else {
            uiState.filteredExpenses.filter { expense ->
                val category = uiState.categories.firstOrNull {
                    it.id == expense.categoryId
                }
                category?.type == selectedCategory
            }
        }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Column {
                        Text("Expense History")
                        Text(
                            "Review expenses by period",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },

                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00BFA5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FBFB))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // ---------------- FILTER PERIOD ----------------
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

            // ---------------- CATEGORY FILTER ----------------
            HistoryInputCard(
                title = "Filter Category",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF00897B)
                    )
                }
            ) {

                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedCategory)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            selectedCategory = "All"
                            expanded = false
                        }
                    )

                    uiState.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.type) },
                            onClick = {
                                selectedCategory = category.type
                                expanded = false
                            }
                        )
                    }
                }
            }

            // ---------------- EMPTY STATE ----------------
            if (displayedExpenses.isEmpty()) {

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

                displayedExpenses.forEach { expense ->

                    val category = uiState.categories.firstOrNull {
                        it.id == expense.categoryId
                    }

                    HistoryExpenseCard(
                        expenseName = expense.name,
                        categoryName = category?.type ?: "Unknown",
                        amount = expense.amount,
                        startDate = expense.startDate,
                        endDate = expense.endDate,
                        description = expense.description,
                        receiptPhotoUri = expense.receiptPhotoUrl,
                        currencySymbol = currency.symbol
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
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
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
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
    receiptPhotoUri: String,
    currencySymbol: String
) {

    var showReceipt by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = expenseName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Text(
                        text = "Category: $categoryName",
                        color = Color(0xFF00897B)
                    )
                }

                Text(
                    text = "$currencySymbol %.2f".format(amount),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BFA5)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("Start Date: $startDate", fontSize = 13.sp)
            Text("End Date: $endDate", fontSize = 13.sp)

            if (description.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(description, color = Color.Gray)
            }

            if (receiptPhotoUri.isNotBlank()) {

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { showReceipt = !showReceipt },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFA5)
                    )
                ) {
                    Text(if (showReceipt) "Hide Receipt" else "Show Receipt")
                }

                if (showReceipt) {

                    Spacer(modifier = Modifier.height(10.dp))

                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        factory = { context ->
                            ImageView(context).apply {
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }
                        },
                        update = { it.setImageURI(Uri.parse(receiptPhotoUri)) }
                    )
                }
            }
        }
    }
}