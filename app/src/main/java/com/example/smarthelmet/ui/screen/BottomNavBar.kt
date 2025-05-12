package com.example.smarthelmet.ui.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smarthelmet.ui.BottomNavigationItem

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = BottomNavigationItem.bottomNavItems
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {if(currentRoute == screen.title){
                    when (screen.selectedIcon) {
                        is ImageVector -> Icon(screen.selectedIcon, contentDescription = screen.title)
                        is Int -> Icon(painterResource(id = screen.selectedIcon), contentDescription = screen.title)
                    }


                }  else {

                    when (screen.unselectedIcon) {
                        is ImageVector -> Icon(screen.unselectedIcon, contentDescription = screen.title)
                        is Int -> Icon(painterResource(id = screen.unselectedIcon), contentDescription = screen.title)
                    }
                } },
                label = { Text(screen.title) },
                selected = currentRoute == screen.title,
                onClick = {
                    if (currentRoute != screen.title) {
                        navController.navigate(screen.title) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
