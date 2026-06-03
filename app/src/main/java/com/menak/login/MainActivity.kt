package com.menak.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.menak.login.data.AppDatabase
import com.menak.login.data.Repository.FirebaseAuthRepository
import com.menak.login.data.Repository.ExpenseRepository
import com.menak.login.data.Repository.CurrencyManagerRepository
import com.menak.login.navigation.AppNavGraph
import com.menak.login.navigation.AuthNavGraph
import com.menak.login.screens.ViewModel.CurrencyViewModel
import com.menak.login.screens.ViewModel.CurrencyViewModelFactory
import com.menak.login.screens.ViewModel.SettingsViewModel
import com.menak.login.ui.AuthViewModel
import com.menak.login.ui.AuthViewModelFactory
import com.menak.login.ui.ExpenseViewModel
import com.menak.login.ui.ExpenseViewModelFactory
import com.menak.login.theme.LoginTheme

class MainActivity : ComponentActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val firestore = FirebaseFirestore.getInstance()

        val authRepository = FirebaseAuthRepository(
            auth = auth,
        )

        val expenseRepository = ExpenseRepository(
            categoryDao = database.categoryDao(),
            expenseDao = database.expenseDao(),
            budgetDao = database.budgetDao(),
            firestore = firestore
        )

        val currencyRepository = CurrencyManagerRepository(applicationContext)

        val authFactory = AuthViewModelFactory(authRepository)
        val expenseFactory = ExpenseViewModelFactory(expenseRepository)
        val currencyFactory = CurrencyViewModelFactory(currencyRepository)

        setContent {

            val authViewModel: AuthViewModel =
                viewModel(factory = authFactory)

            val expenseViewModel: ExpenseViewModel =
                viewModel(factory = expenseFactory)

            val currencyVM: CurrencyViewModel =
                viewModel(factory = currencyFactory)

            val settingsVM: SettingsViewModel =
                viewModel()

            val authUiState by authViewModel.uiState.collectAsState()
            val darkMode by settingsVM.darkMode.collectAsState()

            val isLoggedIn =
                authUiState.isLoggedIn || auth.currentUser != null

            LoginTheme(darkTheme = darkMode) {

                if (isLoggedIn) {

                    val navController = rememberNavController()

                    AppNavGraph(
                        navController = navController,
                        viewModel = expenseViewModel,
                        settingsVM = settingsVM,
                        currencyVM = currencyVM,
                        username = authUiState.loggedInUsername.ifEmpty {
                            auth.currentUser?.email ?: "User"
                        },
                        onLogout = {
                            authViewModel.logout()
                            auth.signOut()
                        }
                    )

                } else {

                    val navController = rememberNavController()

                    AuthNavGraph(
                        navController = navController,
                        viewModel = authViewModel
                    )
                }
            }
        }
    }
}