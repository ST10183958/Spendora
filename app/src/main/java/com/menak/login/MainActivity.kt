package com.menak.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.menak.login.data.AppDatabase
import com.menak.login.data.Repository.AuthRepository
import com.menak.login.data.Repository.ExpenseRepository
import com.menak.login.navigation.AppNavGraph
import com.menak.login.navigation.AuthNavGraph
import com.menak.login.ui.AuthViewModel
import com.menak.login.ui.AuthViewModelFactory
import com.menak.login.ui.ExpenseViewModel
import com.menak.login.ui.ExpenseViewModelFactory
import com.menak.login.theme.LoginTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        val authRepository = AuthRepository(database.userDao())
        val authFactory = AuthViewModelFactory(authRepository)

        val expenseRepository = ExpenseRepository(
            categoryDao = database.categoryDao(),
            expenseDao = database.expenseDao(),
            budgetDao = database.budgetDao()
        )
        val expenseFactory = ExpenseViewModelFactory(expenseRepository)

        setContent {
            LoginTheme {
                val authViewModel: AuthViewModel = viewModel(factory = authFactory)
                val expenseViewModel: ExpenseViewModel = viewModel(factory = expenseFactory)

                val authUiState by authViewModel.uiState.collectAsState()

                if (authUiState.isLoggedIn) {
                    val appNavController = rememberNavController()

                    AppNavGraph(
    navController = appNavController,
    viewModel = expenseViewModel,
    username = authUiState.loggedInUsername,
    onLogout = { authViewModel.logout() }
                    )
                } else {
                    val authNavController = rememberNavController()

                    AuthNavGraph(
                        navController = authNavController,
                        viewModel = authViewModel
                    )
                }
            }
        }
    }
}