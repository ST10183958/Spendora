package com.menak.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.menak.login.R
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    username: String,
    navController: NavController,
    viewModel: ExpenseViewModel,
    onLogout: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun safeProgress(spent: Double, budget: Double): Float {
        if (budget <= 0.0) return 0f
        return (spent / budget).toFloat().coerceIn(0f, 1f)
    }

    val categorySpending = remember(uiState.expenses, uiState.categories) {

        uiState.categories.map { category ->

            val spent = uiState.expenses
                .filter { it.categoryId == category.id }
                .sumOf { it.amount }

            val remaining = max(0.0, 0.0 - spent) // (no budget table yet, so 0 baseline)

            CategorySpendingDashboardItem(
                categoryName = category.type,
                spentAmount = spent,
                remainingAmount = remaining,
                progress = 0f // optional later when budget is wired
            )
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Text(
                    "Spendora Menu",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    color = Color(0xFF00A896)
                )

                NavigationDrawerItem(
                    label = { Text("Add Expense") },
                    selected = false,
                    onClick = {
                        navController.navigate("add_expense")
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Add Category") },
                    selected = false,
                    onClick = {
                        navController.navigate("add_category")
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Budget") },
                    selected = false,
                    onClick = {
                        navController.navigate("budget_screen")
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Analytics") },
                    selected = false,
                    onClick = {
                        navController.navigate("analytics_screen")
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("History") },
                    selected = false,
                    onClick = {
                        navController.navigate("expense_period_list")
                        scope.launch { drawerState.close() }
                    }
                )

                Spacer(Modifier.height(20.dp))

                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = onLogout
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Spendora", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF00A896)
                    )
                )
            },

            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("add_expense") },
                    containerColor = Color(0xFF00A896)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {

                // ---------------- HEADER ----------------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF00A896)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .shadow(6.dp, CircleShape)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.spendora_logo),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Text("Welcome, $username", color = Color.White)
                        Text(
                            "Budget Overview",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }


                Text(
                    "Category Spending",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                categorySpending.forEach { item ->
                    CategorySpendingDashboardCard(
                        title = item.categoryName,
                        spentAmount = item.spentAmount,
                        remainingAmount = item.remainingAmount,
                        progress = item.progress
                    )
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}


data class CategorySpendingDashboardItem(
    val categoryName: String,
    val spentAmount: Double,
    val remainingAmount: Double,
    val progress: Float
)

@Composable
fun CategorySpendingDashboardCard(
    title: String,
    spentAmount: Double,
    remainingAmount: Double,
    progress: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(title, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(6.dp))

            Text("Spent: R %.2f".format(spentAmount))
            Text("Remaining: R %.2f".format(remainingAmount))

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(progress = progress.coerceIn(0f, 1f))
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