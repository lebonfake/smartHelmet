package com.example.smarthelmet.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smarthelmet.ui.screen.IncidentsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Incidents.route) {
        composable(Screen.Incidents.route) {
            IncidentsScreen()
        }

    }
}
