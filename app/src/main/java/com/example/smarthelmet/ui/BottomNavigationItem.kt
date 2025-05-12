package com.example.smarthelmet.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.smarthelmet.R

sealed class  BottomNavigationItem(
    val title : String ,
    val selectedIcon : Any,
    val unselectedIcon : Any,
    val hasNews : Boolean


){
    object  Incidents : BottomNavigationItem("Incidents" ,R.drawable.incidents_filled , R.drawable.incidents_outlined, false);
    object  Helmets : BottomNavigationItem("Helmets" , R.drawable.helmet_filled , R.drawable.helmet_outlined, false);
    companion object {
        val bottomNavItems = listOf(Incidents, Helmets)
    }


}