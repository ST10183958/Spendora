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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.menak.login.R
import kotlinx.coroutines.launch
import kotlin.math.max



//Adam, E. 2026
object DashboardRoutes {
    const val DASHBOARD = "expense_home"
    const val CATEGORIES = "add_category"
    const val ADD_EXPENSE = "add_expense"
    const val HISTORY = "expense_period_list"
    const val ANALYTICS = "analytics_screen"
    const val BUDGET = "budget_screen"

    const val CATEGORY_TOTALS = "category_totals"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    username: String,
    navController: NavController,
    viewModel: ExpenseViewModel,
    onLogout: () -> Unit
) {

    //Adam, E. 2026
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //Adam, E. 2026
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                Text(
                    "Spendora Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF00A896)
                )

                Text(
                    "Welcome, $username",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFF00A896)
                )

                //Adam, E. 2026
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(DashboardRoutes.DASHBOARD) {
                            popUpTo(DashboardRoutes.DASHBOARD) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Categories") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(DashboardRoutes.CATEGORIES)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Add Expense") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(DashboardRoutes.ADD_EXPENSE)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("History") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(DashboardRoutes.HISTORY)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Analytics") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(DashboardRoutes.ANALYTICS)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Budget Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(DashboardRoutes.BUDGET)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                //Adam, E. 2026
                HorizontalDivider(modifier = Modifier.padding(16.dp))

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    colors = androidx.compose.material3.NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(0xFFFFCDD2),
                        unselectedTextColor = Color(0xFFD32F2F)
                    )
                )
            }
        }
    ) {

        //Adam, E. 2026
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Spendora", color = Color.White) },
                    navigationIcon = {
                        androidx.compose.material3.IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(Icons.Default.List, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    //Android Develops, (n.d)
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF00A896)
                    )
                )
            },

            //Android Develops, (n.d)
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(DashboardRoutes.ADD_EXPENSE) },
                    containerColor = Color(0xFF00A896)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            },

            //Android Develops, (n.d)
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    //Android Develops, (n.d)
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    //Android Develops, (n.d)
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(DashboardRoutes.CATEGORIES) },
                        icon = { Icon(Icons.Default.List, contentDescription = "Categories") },
                        label = { Text("Categories") }
                    )
                    //Android Develops, (n.d)
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(DashboardRoutes.HISTORY) },
                        icon = { Icon(Icons.Default.History, contentDescription = "History") },
                        label = { Text("History") }
                    )
                    //Android Develops, (n.d)
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(DashboardRoutes.ANALYTICS) },
                        icon = { Icon(Icons.Default.Analytics, contentDescription = "Analytics") },
                        label = { Text("Analytics") }
                    )
                }
            }
        ) { paddingValues ->
            // Main content with scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF00A896))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
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
                                    "R %.2f".format(uiState.monthlyBudgetAmount),
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val monthlyProgress = when {
                                uiState.monthlyBudgetAmount > 0.0 -> (uiState.monthlySpentAmount / uiState.monthlyBudgetAmount).toFloat().coerceIn(0f, 1f)
                                else -> 0f
                            }

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
                                    "Spent: R %.2f".format(uiState.monthlySpentAmount),
                                    color = Color.Red
                                )

                                Text(
                                    "Remaining: R %.2f".format(max(uiState.monthlyRemainingAmount, 0.0)),
                                    color = Color(0xFF00A896)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate(DashboardRoutes.BUDGET) },
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
                        onClick = { navController.navigate(DashboardRoutes.CATEGORIES) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text("+ Add New Category")
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
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


//Title: Androidx.compose.material3
//Author: Android Develops
//Date: (n.d)
//Version: 1
//Availability: https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ModalNavigationDrawer(androidx.compose.material3.DrawerState,androidx.compose.ui.Modifier,androidx.compose.material3.DrawerProperties,kotlin.Boolean,kotlin.Function0,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp,kotlin.Function1)


//Title: Sandbox
//Author: Adam, E
//Date: 11 February 2026
//Version: 1
//Availability: https://github.com/PROG7313-2026-EMDBN/Sandbox