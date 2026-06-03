package com.menak.login.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.menak.login.ui.*
import com.menak.login.screens.*
import com.menak.login.screens.ViewModel.CurrencyViewModel
import com.menak.login.screens.ViewModel.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    username: String,
    onLogout: () -> Unit,
    settingsVM: SettingsViewModel,
    currencyVM: CurrencyViewModel,
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
                currencyVM = currencyVM,
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
                onAddNewCategoryClick = { navController.navigate("add_category") },
                currencyVM = currencyVM
            )
        }

        composable("budget_screen") {
            BudgetScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                currencyVM = currencyVM

            )
        }

        // ✅ FIXED HERE
        composable("expense_period_list") {
            ExpensePeriodListScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                currencyVM = currencyVM
            )
        }

        composable("category_totals") {
            CategoryTotalsScreen(
                viewModel = viewModel,
                currencyVM = currencyVM
            )

        }

        // ✅ FIXED HERE
        composable("analytics_screen") {
            AnalyticsScreen(
                navController = navController,
                viewModel = viewModel,
                currencyVM = currencyVM
            )
        }

        composable("currency_settings") {
            CurrencySettingsScreen(
                navController = navController,
                currencyVM = currencyVM
            )
        }

        composable("help_screen") {
            HelpScreen(navController = navController)
        }

        composable("settings_screen") {
            SettingsScreen(
                navController = navController,
                settingsVM = settingsVM
            )
        }
    }
}