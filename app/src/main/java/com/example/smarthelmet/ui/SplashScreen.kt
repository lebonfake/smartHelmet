package com.example.smarthelmet.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smarthelmet.R
import kotlinx.coroutines.delay



@Composable
fun SplashScreen (navController: NavHostController){
    LaunchedEffect(true) {
        delay(2000)
        navController.navigate(BottomNavigationItem.Incidents.title) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White),
        contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(
            R.drawable.smart_helmet_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(320.dp), // adjust size as needed
            contentScale = ContentScale.Fit)
    }
}