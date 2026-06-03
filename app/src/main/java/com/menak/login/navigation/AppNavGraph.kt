package com.menak.login.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.padding
import com.menak.login.ui.*
import com.menak.login.screens.*
import com.menak.login.screens.ViewModel.CurrencyViewModel
import com.menak.login.screens.ViewModel.SettingsViewModel
import com.menak.login.ui.components.BottomBar

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

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "expense_home",
            modifier = Modifier.padding(padding)
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
}