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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.menak.login.util.copyImageToInternalStorage

@Composable
fun AddCategoryScreen(
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedUri = copyImageToInternalStorage(context, it)
            if (savedUri != null) {
                viewModel.onCategoryIconUriChange(savedUri)
            } else {
                viewModel.setMessage("Failed to save selected image")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add Category",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.categoryType,
                    onValueChange = viewModel::onCategoryTypeChange,
                    label = { Text("Category Type") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Category Icon")
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.categoryIconUri.isNotEmpty()) {
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                layoutParams = android.view.ViewGroup.LayoutParams(200, 200)
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }
                        },
                        update = { imageView ->
                            try {
                                imageView.setImageURI(Uri.parse(uiState.categoryIconUri))
                            } catch (_: Exception) {
                                imageView.setImageDrawable(null)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.addCategory() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Category")
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.message.isNotEmpty()) {
                    Text(uiState.message)
                }
            }
        }
    }
}