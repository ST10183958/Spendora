package com.menak.login.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HelpScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFB))
            .verticalScroll(rememberScrollState())
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    color = Color(0xFF00BFA5),
                    shape = RoundedCornerShape(
                        bottomStart = 28.dp,
                        bottomEnd = 28.dp
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 28.dp,
                    start = 12.dp,
                    end = 12.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Help & Budgeting Tips",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Learn how to manage your finances more effectively using Spendora.",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            HelpTipCard(
                title = "Set a Monthly Budget",
                description = "Create a realistic monthly budget based on your income and fixed expenses."
            )

            HelpTipCard(
                title = "Track Every Expense",
                description = "Record expenses as soon as they happen to ensure accurate spending records."
            )

            HelpTipCard(
                title = "Use Categories",
                description = "Organise expenses into categories such as Food, Transport, Entertainment and Utilities."
            )

            HelpTipCard(
                title = "Review Spending Weekly",
                description = "Review your spending trends every week and identify areas where you can save."
            )

            HelpTipCard(
                title = "Build an Emergency Fund",
                description = "Set aside money each month to prepare for unexpected expenses and emergencies."
            )

            HelpTipCard(
                title = "Avoid Impulse Purchases",
                description = "Wait before making non-essential purchases and consider whether they fit within your budget."
            )

            HelpTipCard(
                title = "Use Analytics Regularly",
                description = "Check the Analytics screen to understand spending patterns and make informed decisions."
            )

            HelpTipCard(
                title = "Monitor Category Limits",
                description = "Keep an eye on category budgets to avoid overspending in specific areas."
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HelpTipCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                color = Color(0xFF00A896)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp
            )
        }
    }
}