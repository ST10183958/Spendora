package com.menak.login.ui

import android.graphics.Color as AndroidColor
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
import kotlin.math.max

@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: ExpenseViewModel
) {
    val analytics by viewModel.analyticsUiState.collectAsState()

    fun safeFloat(value: Double): Float =
        if (value.isFinite()) value.toFloat() else 0f

    fun safeProgress(value: Double, max: Double): Float =
        if (max > 0.0 && value.isFinite() && max.isFinite())
            (value / max).toFloat().coerceIn(0f, 1f)
        else 0f

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
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Text("Analytics", color = Color.White, fontSize = 24.sp)
                    }

                    Text(
                        "Spending insights",
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

                Spacer(Modifier.height(20.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text("Spending Goals", fontSize = 16.sp)

                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Minimum Goal", color = Color(0xFF00C853))
                            Text("R %.2f".format(analytics.minGoal), color = Color(0xFF00C853))
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Maximum Limit", color = Color(0xFFFF5252))
                            Text("R %.2f".format(analytics.maxGoal), color = Color(0xFFFF5252))
                        }

                        Spacer(Modifier.height(12.dp))

                        val progress = safeProgress(
                            analytics.totalSpent,
                            analytics.maxGoal
                        )

                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier.fillMaxWidth(),
                            color = if (analytics.totalSpent <= analytics.minGoal)
                                Color(0xFF00C853)
                            else
                                Color(0xFFFF5252)
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Current: R %.2f".format(analytics.totalSpent),
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

4
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text("Goal Performance (Monthly)", fontSize = 16.sp)

                        Spacer(Modifier.height(12.dp))

                        val score = analytics.goalScore.coerceIn(0.0, 100.0)

                        Text(
                            "Score: %.0f%%".format(score),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (score >= 70) Color(0xFF00C853) else Color(0xFFFF5252)
                        )

                        Spacer(Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = (score / 100.0).toFloat(),
                            modifier = Modifier.fillMaxWidth(),
                            color = if (score >= 70) Color(0xFF00C853) else Color(0xFFFF5252)
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Within Goal: %.0f%%".format(analytics.daysWithinGoalPercent),
                                color = Color(0xFF00C853)
                            )

                            Text(
                                "Over Goal: %.0f%%".format(analytics.daysOverGoalPercent),
                                color = Color(0xFFFF5252)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}