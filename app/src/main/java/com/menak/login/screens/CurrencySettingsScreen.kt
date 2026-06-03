package com.menak.login.screens

import android.icu.number.Precision.currency
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.menak.login.data.CurrencyType
import com.menak.login.data.Repository.CurrencyManagerRepository
import com.menak.login.screens.ViewModel.CurrencyViewModel
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrencySettingsScreen(
    navController: NavController,
    currencyVM: CurrencyViewModel
) {
    val selectedCurrency = currencyVM.currency.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        color = Color(0xFF00BFA5),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(top = 28.dp, start = 12.dp, end = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "Currency Settings",
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }


                }
            }

            Column {
                CurrencyType.entries.forEach { currency ->
                    Row {
                        RadioButton(
                            selected = selectedCurrency == currency,
                            onClick = {
                                currencyVM.updateCurrency(currency)
                            }
                        )

                        Text(
                            text = "${currency.code} (${currency.symbol})"
                        )
                    }
                }
            }
        }
    }
}