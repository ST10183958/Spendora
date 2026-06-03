package com.menak.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.menak.login.screens.ViewModel.CurrencyViewModel

@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: ExpenseViewModel,
    currencyVM: CurrencyViewModel
) {

    // ---------------- SAFE STATE HANDLING ----------------
    val analyticsState = viewModel.analyticsUiState.collectAsState()
    val analytics = analyticsState.value

    val currency = currencyVM.currency.value

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

            // ---------------- HEADER ----------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Color(0xFF00BFA5),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "Analytics",
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }

                    Text(
                        text = "Spending insights",
                        color = Color(0xFFE0F2F1),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // ---------------- STATS ----------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    AnalyticsStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Spent",
                        value = "${currency.symbol} %.2f".format(analytics.totalSpent)
                    )

                    AnalyticsStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Daily Avg",
                        value = "${currency.symbol} %.2f".format(analytics.dailyAverage)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------------- PIE CHART ----------------
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Category Breakdown",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AndroidView(
                            factory = { context ->
                                PieChart(context).apply {
                                    description.isEnabled = false
                                    legend.isEnabled = false
                                }
                            },
                            update = { chart ->

                                val entries = analytics.categoryBreakdown.map {
                                    PieEntry(it.amount.toFloat(), it.name)
                                }

                                val dataSet = PieDataSet(entries, "")
                                dataSet.colors = analytics.categoryBreakdown.map {
                                    it.color.toInt()
                                }

                                chart.data = PieData(dataSet)
                                chart.invalidate()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------------- BAR CHART ----------------
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Daily Spending",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AndroidView(
                            factory = { context ->
                                BarChart(context).apply {
                                    description.isEnabled = false
                                    legend.isEnabled = false
                                    axisRight.isEnabled = false
                                }
                            },
                            update = { chart ->

                                val entries = analytics.dailySpending.mapIndexed { index, item ->
                                    BarEntry(index.toFloat(), item.amount.toFloat())
                                }

                                val dataSet = BarDataSet(entries, "Daily Spending")

                                chart.data = BarData(dataSet)
                                chart.invalidate()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------------- MONTHLY COMPARISON ----------------
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00BFA5))
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Monthly Comparison",
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text("This Month", color = Color(0xFFB2DFDB))
                                Text(
                                    "${currency.symbol} %.2f".format(analytics.thisMonthTotal),
                                    color = Color.White
                                )
                            }

                            Column {
                                Text("Last Month", color = Color(0xFFB2DFDB))
                                Text(
                                    "${currency.symbol} %.2f".format(analytics.lastMonthTotal),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AnalyticsStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00BFA5))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = Color(0xFFD0F3EE),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}