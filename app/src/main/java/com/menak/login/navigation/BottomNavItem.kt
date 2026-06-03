package com.menak.login.navigation

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
) {
    object Home : BottomNavItem(
        "expense_home",
        android.R.drawable.ic_menu_view,
        "Home"
    )

    object Categories : BottomNavItem(
        "add_category",
        android.R.drawable.ic_menu_sort_by_size,
        "Categories"
    )

    object History : BottomNavItem(
        "expense_period_list",
        android.R.drawable.ic_menu_recent_history,
        "History"
    )

    object Analytics : BottomNavItem(
        "analytics_screen",
        android.R.drawable.ic_menu_info_details,
        "Analytics"
    )
}