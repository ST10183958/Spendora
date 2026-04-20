package com.menak.login.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.menak.login.R
import kotlin.math.max

@Composable
fun MainDashboardScreen(
    username: String,
    navController: NavController,
    viewModel: ExpenseViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val monthlyBudget = uiState.monthlyBudgetAmount
    val monthlySpent = uiState.monthlySpentAmount
    val monthlyRemaining = uiState.monthlyRemainingAmount

    val monthlyProgress = when {
        monthlyBudget > 0.0 -> (monthlySpent / monthlyBudget).toFloat().coerceIn(0f, 1f)
        else -> 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
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
                    .background(Color(0xFF00A896))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    Text("Spendora", color = Color.White, fontSize = 18.sp)
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .shadow(8.dp, CircleShape)
                            .background(Color(0xFF00A896), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.spendora_logo),
                            contentDescription = "Spendora Logo",
                            modifier = Modifier.size(45.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "Welcome back, $username!",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Text(
                        "Your Budget Overview",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(uiState.currentMonthLabel, fontSize = 12.sp)
                                Text("Monthly Budget", fontSize = 18.sp)
                            }

                            Text(
                                "R %.2f".format(monthlyBudget),
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { monthlyProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = Color(0xFF00A896)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Spent: R %.2f".format(monthlySpent),
                                color = Color.Red
                            )

                            Text(
                                "Remaining: R %.2f".format(max(monthlyRemaining, 0.0)),
                                color = Color(0xFF00A896)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("budget_screen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00A896)
                    )
                ) {
                    Text("Set Budget Goal", color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("Category Spending", fontSize = 18.sp)

                Spacer(modifier = Modifier.height(10.dp))

                uiState.dashboardCategorySpending.forEach { item ->
                    CategorySpendingDashboardCard(
                        title = item.categoryName,
                        spentAmount = item.spentAmount,
                        remainingAmount = item.remainingAmount,
                        progress = item.progress
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navController.navigate("add_category") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("+ Add New Category")
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("add_expense") },
            containerColor = Color(0xFF00A896),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Expense")
        }

        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )

            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("add_category") },
                icon = { Icon(Icons.Default.List, contentDescription = "Categories") },
                label = { Text("Categories") }
            )

            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("expense_period_list") },
                icon = { Icon(Icons.Default.History, contentDescription = "History") },
                label = { Text("History") }
            )

            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("category_totals") },
                icon = { Icon(Icons.Default.Analytics, contentDescription = "Analytics") },
                label = { Text("Analytics") }
            )
        }
    }
}

@Composable
fun CategorySpendingDashboardCard(
    title: String,
    spentAmount: Double,
    remainingAmount: Double?,
    progress: Float
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title)

                if (remainingAmount != null) {
                    Text("R %.2f left".format(max(remainingAmount, 0.0)))
                } else {
                    Text("R %.2f spent".format(spentAmount))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFF00A896)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Spent: R %.2f".format(spentAmount), color = Color(0xFF00A896))
        }
    }
}