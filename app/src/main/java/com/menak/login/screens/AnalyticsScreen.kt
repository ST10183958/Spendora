package com.menak.login.ui

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: ExpenseViewModel
) {
    val analytics by viewModel.analyticsUiState.collectAsState()

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
                            text = "Analytics",
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }

                    Text(
                        text = "Spending insights",
                        color = Color(0xFFE0F2F1),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AnalyticsStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Spent",
                        value = "R %.2f".format(analytics.totalSpent)
                    )

                    AnalyticsStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Daily Avg",
                        value = "R %.2f".format(analytics.dailyAverage)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Category Breakdown",
                            fontSize = 16.sp,
                            color = Color(0xFF1A1A2E)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AndroidView(
                            factory = { context ->
                                PieChart(context).apply {
                                    description.isEnabled = false
                                    legend.isEnabled = false
                                    setUsePercentValues(false)
                                    setEntryLabelColor(AndroidColor.BLACK)
                                    setHoleColor(AndroidColor.WHITE)
                                }
                            },
                            update = { chart ->
                                val entries = analytics.categoryBreakdown.map {
                                    PieEntry(it.amount.toFloat(), it.name)
                                }

                                val colors = analytics.categoryBreakdown.map {
                                    it.color.toInt()
                                }

                                val dataSet = PieDataSet(entries, "")
                                dataSet.colors = colors
                                dataSet.sliceSpace = 2f

                                val data = PieData(dataSet)
                                data.setDrawValues(false)

                                chart.data = data
                                chart.invalidate()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val leftItems = analytics.categoryBreakdown.filterIndexed { index, _ -> index % 2 == 0 }
                        val rightItems = analytics.categoryBreakdown.filterIndexed { index, _ -> index % 2 == 1 }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                leftItems.forEach { item ->
                                    AnalyticsLegendItem(item)
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                rightItems.forEach { item ->
                                    AnalyticsLegendItem(item)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Daily Spending",
                            fontSize = 16.sp,
                            color = Color(0xFF1A1A2E)
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
                                val data = BarData(dataSet)
                                data.barWidth = 0.6f

                                chart.data = data
                                chart.xAxis.apply {
                                    granularity = 1f
                                    setDrawGridLines(false)
                                }
                                chart.axisLeft.setDrawGridLines(false)
                                chart.invalidate()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00BFA5)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Monthly Comparison",
                            color = Color.White,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("This Month", color = Color(0xFFB2DFDB))
                                Text(
                                    "R %.2f".format(analytics.thisMonthTotal),
                                    color = Color.White
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Last Month", color = Color(0xFFB2DFDB))
                                Text(
                                    "R %.2f".format(analytics.lastMonthTotal),
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
private fun AnalyticsStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00BFA5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = Color(0xFFD0F3EE),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun AnalyticsLegendItem(item: CategoryAnalyticsItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color(item.color), CircleShape)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "${item.name}   R %.0f".format(item.amount),
            color = Color(item.color),
            fontSize = 13.sp
        )
    }
}