
package com.menak.login.ui


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.menak.login.navigation.Routes
import kotlinx.coroutines.launch

//Adam, E. 2026
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScaffold(
    navController: NavHostController,
    username: String,
    onLogout: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {

    //Adam, E. 2026
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //Adam, E. 2026
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    //Adam, E. 2026
    val topLevelRoutes = setOf(
        Routes.DASHBOARD,
        Routes.CATEGORIES,
        Routes.HISTORY,
        Routes.ANALYTICS,
        Routes.BUDGET,
        Routes.CATEGORY_TOTALS
    )

    //Adam, E. 2026
    val isTopLevel = currentRoute in topLevelRoutes
    val canGoBack = navController.previousBackStackEntry != null


    //Adam, E. 2026
    val title = when (currentRoute) {
        Routes.DASHBOARD -> "Dashboard"
        Routes.CATEGORIES -> "Categories"
        Routes.ADD_EXPENSE -> "Add Expense"
        Routes.HISTORY -> "Expense History"
        Routes.ANALYTICS -> "Analytics"
        Routes.BUDGET -> "Budget Settings"
        Routes.CATEGORY_TOTALS -> "Category Totals"
        else -> "Spendora"
    }

    //Adam, E. 2026
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isTopLevel,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    "Spendora Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    "Welcome, $username",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                //Adam, E. 2026
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = currentRoute == Routes.DASHBOARD,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.DASHBOARD) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Categories") },
                    selected = currentRoute == Routes.CATEGORIES,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.CATEGORIES) { launchSingleTop = true }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Add Expense") },
                    selected = currentRoute == Routes.ADD_EXPENSE,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.ADD_EXPENSE) { launchSingleTop = true }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("History") },
                    selected = currentRoute == Routes.HISTORY,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.HISTORY) { launchSingleTop = true }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Analytics") },
                    selected = currentRoute == Routes.ANALYTICS,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.ANALYTICS) { launchSingleTop = true }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                //Adam, E. 2026
                NavigationDrawerItem(
                    label = { Text("Budget Settings") },
                    selected = currentRoute == Routes.BUDGET,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.BUDGET) { launchSingleTop = true }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

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
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                        unselectedTextColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    ) {

        //Adam, E. 2026
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (isTopLevel) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        } else if (canGoBack) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },

                    //Android Develops, (n.d)
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF00A896),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            content(Modifier.padding(paddingValues))
        }
    }
}


//Title: Sandbox
//Author: Adam, E
//Date: 11 February 2026
//Version: 1
//Availability: https://github.com/PROG7313-2026-EMDBN/Sandbox



//Title: Androidx.compose.material3
//Author: Android Develops
//Date: (n.d)
//Version: 1
//Availability: https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ModalNavigationDrawer(androidx.compose.material3.DrawerState,androidx.compose.ui.Modifier,androidx.compose.material3.DrawerProperties,kotlin.Boolean,kotlin.Function0,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp,kotlin.Function1)