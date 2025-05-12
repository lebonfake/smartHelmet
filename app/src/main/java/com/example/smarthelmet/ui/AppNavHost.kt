package com.example.smarthelmet.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smarthelmet.ui.screen.BottomNavBar
import com.example.smarthelmet.ui.screen.HelmetScreen
import com.example.smarthelmet.ui.screen.IncidentsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavigationItem.Incidents.title,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationItem.Incidents.title) {
                IncidentsScreen()
            }
            composable(BottomNavigationItem.Helmets.title) {
                HelmetScreen()
            }

        }
    }
}
