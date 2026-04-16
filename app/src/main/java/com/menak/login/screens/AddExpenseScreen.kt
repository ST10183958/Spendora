package com.menak.login.ui

import android.net.Uri
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.menak.login.util.copyImageToInternalStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    //launchers
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedUri = copyImageToInternalStorage(context, it)
            if (savedUri != null) {
                viewModel.onExpenseIconUriChange(savedUri)
            } else {
                viewModel.setMessage("Failed to save selected expense icon")
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

    val selectedCategoryName = uiState.categories
        .firstOrNull { it.id == uiState.selectedCategoryId }
        ?.type ?: "Select Category"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add Expense",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.expenseName,
                    onValueChange = viewModel::onExpenseNameChange,
                    label = { Text("Expense Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                                    viewModel.onSelectedCategoryChange(category.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.expenseAmount,
                    onValueChange = viewModel::onExpenseAmountChange,
                    label = { Text("Expense Amount") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.expenseStartDate,
                    onValueChange = viewModel::onExpenseStartDateChange,
                    label = { Text("Expense Start Date") },
                    placeholder = { Text("e.g. 2026-04-15") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.expenseEndDate,
                    onValueChange = viewModel::onExpenseEndDateChange,
                    label = { Text("Expense End Date") },
                    placeholder = { Text("e.g. 2026-04-20") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.expenseDescription,
                    onValueChange = viewModel::onExpenseDescriptionChange,
                    label = { Text("Expense Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Expense Icon")
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.expenseIconUrl.isNotEmpty()) {
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                layoutParams = android.view.ViewGroup.LayoutParams(200, 200)
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.addExpense() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Expense")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { receiptLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Receipt Photo")
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.receiptPhotoUrl.isNotEmpty()) {
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                layoutParams = android.view.ViewGroup.LayoutParams(200, 200)
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

                if (uiState.message.isNotEmpty()) {
                    Text(uiState.message)
                }
            }
        }
    }
}