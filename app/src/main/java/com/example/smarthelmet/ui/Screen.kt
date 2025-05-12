package com.example.smarthelmet.ui

sealed class Screen(val route: String) {
    object Helmets : Screen("helmets")
    object Incidents : Screen("incidents")
}
