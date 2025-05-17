package com.example.smarthelmet.ui


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smarthelmet.data.repository.HelmetRepository
import com.example.smarthelmet.data.repository.WorkerRepository
import com.example.smarthelmet.ui.screen.BottomNavBar
import com.example.smarthelmet.ui.screen.Helmets.HelmetScreen
import com.example.smarthelmet.ui.screen.Helmets.HelmetsViewModel
import com.example.smarthelmet.ui.screen.Workers.WorkersScreen
import com.example.smarthelmet.ui.screen.Workers.WorkersViewModel
import com.example.smarthelmet.ui.screen.incidents.IncidentsScreen
import com.example.smarthelmet.ui.screen.incidents.IncidentsViewModel


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Create a shared ViewModel instance that will be reused across navigation
    val incidentsViewModel: IncidentsViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute != "splash"

    Scaffold(
        bottomBar = {
            if(showBottomBar){
            BottomNavBar(navController = navController)}
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationItem.Incidents.title) {
                // Pass the shared ViewModel instance
                IncidentsScreen()
            }
            composable(BottomNavigationItem.Helmets.title) {
                HelmetScreen(HelmetsViewModel(HelmetRepository()))
            }

            composable(BottomNavigationItem.Workers.title) {
                WorkersScreen(WorkersViewModel(WorkerRepository()))
            }

            composable("splash"){
                SplashScreen(navController)
            }
        }
    }
}