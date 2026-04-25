package com.menak.login.ui

import android.net.Uri
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.menak.login.ui.components.DatePickerField
import com.menak.login.util.copyImageToInternalStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel,
    onBackClick: () -> Unit,
    onAddNewCategoryClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var categoryExpanded by remember { mutableStateOf(false) }

    val selectedCategoryName = uiState.categories
        .firstOrNull { it.id == uiState.selectedCategoryId }
        ?.type ?: ""

    val context = androidx.compose.ui.platform.LocalContext.current

    val expenseIconLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedUri = copyImageToInternalStorage(context, it)
            if (savedUri != null) {
                viewModel.onExpenseIconUriChange(savedUri)
            } else {
                viewModel.setMessage("Failed to save expense icon")
            }
        }
    }

    val receiptLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedUri = copyImageToInternalStorage(context, it)
            if (savedUri != null) {
                viewModel.onReceiptPhotoUriChange(savedUri)
            } else {
                viewModel.setMessage("Failed to save receipt photo")
            }
        }
    }

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
                        text = "Add Expense",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Track your spending",
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

                ExpenseInputCard(
                    title = "Expense Name",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Title,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    OutlinedTextField(
                        value = uiState.expenseName,
                        onValueChange = viewModel::onExpenseNameChange,
                        placeholder = { Text("Enter expense name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = expenseTextFieldColors()
                    )
                }

                ExpenseInputCard(
                    title = "Amount",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    OutlinedTextField(
                        value = uiState.expenseAmount,
                        onValueChange = viewModel::onExpenseAmountChange,
                        placeholder = { Text("$ 0.00") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp),
                        colors = expenseTextFieldColors()
                    )
                }

                ExpenseInputCard(
                    title = "Date Range",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    DatePickerField(
                        label = "Expense Start Date",
                        value = uiState.expenseStartDate,
                        onDateSelected = viewModel::onExpenseStartDateChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DatePickerField(
                        label = "Expense End Date",
                        value = uiState.expenseEndDate,
                        onDateSelected = viewModel::onExpenseEndDateChange
                    )
                }

                ExpenseInputCard(
                    title = "Category",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategoryName,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select category") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = expenseTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            uiState.categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.type) },
                                    onClick = {
                                        viewModel.onSelectedCategoryChange(category.id)
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onAddNewCategoryClick,
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text(
                            text = "+ Add A New Category (Optional)",
                            color = Color(0xFF00BFA5)
                        )
                    }
                }

                ExpenseInputCard(
                    title = "Description (Optional)",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    OutlinedTextField(
                        value = uiState.expenseDescription,
                        onValueChange = viewModel::onExpenseDescriptionChange,
                        placeholder = { Text("Add a note about this expense...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = expenseTextFieldColors()
                    )
                }

                ExpenseInputCard(
                    title = "Expense Icon",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    Button(
                        onClick = { expenseIconLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFA5))
                    ) {
                        Text("Select Expense Icon", color = Color.White)
                    }

                    if (uiState.expenseIconUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        AndroidView(
                            modifier = Modifier.size(100.dp),
                            factory = { ctx ->
                                ImageView(ctx).apply {
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                }
                            },
                            update = { imageView ->
                                try {
                                    imageView.setImageURI(Uri.parse(uiState.expenseIconUrl))
                                } catch (_: Exception) {
                                    imageView.setImageDrawable(null)
                                }
                            }
                        )
                    }
                }

                ExpenseInputCard(
                    title = "Receipt Photo (Optional)",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    }
                ) {
                    Button(
                        onClick = { receiptLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFA5))
                    ) {
                        Text("Select Receipt Photo", color = Color.White)
                    }

                    if (uiState.receiptPhotoUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        AndroidView(
                            modifier = Modifier.size(100.dp),
                            factory = { ctx ->
                                ImageView(ctx).apply {
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                }
                            },
                            update = { imageView ->
                                try {
                                    imageView.setImageURI(Uri.parse(uiState.receiptPhotoUrl))
                                } catch (_: Exception) {
                                    imageView.setImageDrawable(null)
                                }
                            }
                        )
                    }
                }

                if (uiState.message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        Button(
            onClick = { viewModel.addExpense() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 20.dp
                )
                .height(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00BFA5)
            )
        ) {
            Text(
                text = "Add Expense",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ExpenseInputCard(
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
private fun expenseTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color(0xFFF1F3F4),
    unfocusedContainerColor = Color(0xFFF1F3F4),
    disabledContainerColor = Color(0xFFF1F3F4),
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
)