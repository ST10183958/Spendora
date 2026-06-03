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
import com.menak.login.navigation.AppNavGraph
import com.menak.login.navigation.AuthNavGraph
import com.menak.login.theme.LoginTheme
import com.menak.login.ui.*
import com.menak.login.screens.ViewModel.SettingsViewModel

class MainActivity : ComponentActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val firestore = FirebaseFirestore.getInstance()

        val authRepository = FirebaseAuthRepository(auth)

        val expenseRepository = ExpenseRepository(
            categoryDao = database.categoryDao(),
            expenseDao = database.expenseDao(),
            budgetDao = database.budgetDao(),
            firestore = firestore
        )

        val authFactory = AuthViewModelFactory(authRepository)
        val expenseFactory = ExpenseViewModelFactory(expenseRepository)

        setContent {

            val authViewModel: AuthViewModel = viewModel(factory = authFactory)
            val expenseViewModel: ExpenseViewModel = viewModel(factory = expenseFactory)

            val authUiState by authViewModel.uiState.collectAsState()
            val firebaseUser = auth.currentUser

            val settingsVm: SettingsViewModel = viewModel()
            val darkMode by settingsVm.darkMode.collectAsState()

            val isLoggedIn = authUiState.isLoggedIn || firebaseUser != null

            LoginTheme(darkTheme = darkMode) {

                if (isLoggedIn) {

                    val username =
                        authUiState.loggedInUsername.ifEmpty {
                            firebaseUser?.email ?: "User"
                        }

                    val navController = rememberNavController()

                    AppNavGraph(
                        navController = navController,
                        viewModel = expenseViewModel,
                        username = username,
                        onLogout = {
                            authViewModel.logout()
                            auth.signOut()
                        },
                        settingsVM = settingsVm
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