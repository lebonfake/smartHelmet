package com.example.smarthelmet.ui.screen.Helmets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.smarthelmet.R

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthelmet.model.Helmet

@Composable
fun HelmetScreen(viewModel: HelmetsViewModel = viewModel()) {
  val helmets by viewModel.helmets.collectAsState()
Column(modifier = Modifier.padding(12.dp)) {
  Text(text = "Helmets",
    color = Color(0xFF47A0F6) ,
    fontSize = 30.sp,
    style = MaterialTheme.typography.titleLarge,
    fontWeight = FontWeight.Bold)

  Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(12.dp))

  LazyColumn(
  modifier = Modifier.fillMaxSize()
) {
  items(helmets) { helmet ->
    HelmetListItem(helmet) {
      Log.d("HelmetScreen", "Helmet clicked: ${helmet.helmetId}")
    }
  }
} }

}





@Composable
fun HelmetListItem(
  helmet: Helmet,
  onClick: () -> Unit = {}
) {
  Log.d("helmet", "HelmetListItem: $helmet")

  // Define our custom colors
  val BlueAccent = Color(0xFF47A0F6)
  val DarkText = Color.Black
  val LightBlue = Color(0xFFE6F1FE)

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp, horizontal = 4.dp)
      .shadow(
        elevation = 3.dp,
        shape = RoundedCornerShape(12.dp),
        spotColor = BlueAccent.copy(alpha = 0.15f)
      )
      .clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Helmet Icon
      Box(
        modifier = Modifier
          .size(48.dp)
          .background(LightBlue, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          painter = painterResource(R.drawable.helmet_filled),
          contentDescription = "Helmet",
          tint = BlueAccent,
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Helmet ID
      Text(
        text = helmet.helmetId ?: "Unknown ID",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = DarkText,
        modifier = Modifier.weight(1f)
      )

      // Arrow or action indicator
      Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = "View Details",
        tint = BlueAccent.copy(alpha = 0.7f)
      )
    }
  }
}

