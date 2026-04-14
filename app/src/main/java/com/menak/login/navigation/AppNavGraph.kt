package com.menak.login.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.menak.login.ui.AddCategoryScreen
import com.menak.login.ui.AddExpenseScreen
import com.menak.login.ui.ExpenseHomeScreen
import com.menak.login.ui.ExpenseViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "expense_home",
        modifier = modifier
    ) {
        composable("expense_home") {
            ExpenseHomeScreen(
                navController = navController,
                viewModel = viewModel,
                onLogout = onLogout
            )
        }

        composable("add_category") {
            AddCategoryScreen(viewModel = viewModel)
        }

        composable("add_expense") {
            AddExpenseScreen(viewModel = viewModel)
        }
    }
}