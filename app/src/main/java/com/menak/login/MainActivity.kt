package com.menak.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.menak.login.data.AppDatabase
import com.menak.login.data.AuthRepository
import com.menak.login.screens.AuthScreen
import com.menak.login.screens.AuthViewModel
import com.menak.login.screens.AuthViewModelFactory
import com.menak.login.ui.theme.RoomLoginTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = AuthRepository(database.userDao())
        val factory = AuthViewModelFactory(repository)

        setContent {
            RoomLoginTheme {
                val viewModel: AuthViewModel = viewModel(factory = factory)
                AuthScreen(viewModel = viewModel)
            }
        }
    }
}