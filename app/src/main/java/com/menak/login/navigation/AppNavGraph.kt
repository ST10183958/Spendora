package com.menak.login.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.menak.login.ui.AddCategoryScreen
import com.menak.login.ui.AddExpenseScreen
import com.menak.login.ui.AnalyticsScreen
import com.menak.login.ui.BudgetScreen
import com.menak.login.ui.CategoryTotalsScreen
import com.menak.login.ui.ExpensePeriodListScreen
import com.menak.login.ui.ExpenseViewModel
import com.menak.login.ui.MainDashboardScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    username: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "expense_home",
        modifier = modifier
    ) {
        composable("expense_home") {
            MainDashboardScreen(
                username = username,
                navController = navController,
                viewModel = viewModel,
                onLogout = onLogout
            )
        }

        composable("add_category") {
            AddCategoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("add_expense") {
            AddExpenseScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onAddNewCategoryClick = { navController.navigate("add_category") }
            )
        }

        composable("budget_screen") {
            BudgetScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("expense_period_list") {
            ExpensePeriodListScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("category_totals") {
            CategoryTotalsScreen(viewModel = viewModel)
        }

        composable("analytics_screen") {
            AnalyticsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}