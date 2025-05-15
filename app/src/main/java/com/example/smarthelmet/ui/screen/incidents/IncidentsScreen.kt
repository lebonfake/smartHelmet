package com.example.smarthelmet.ui.screen.incidents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthelmet.model.Incident
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.smarthelmet.R

@Composable
fun IncidentsScreen(viewModel: IncidentsViewModel = viewModel()) {
    val incidents by viewModel.incidents.collectAsState()
    val helmetId = viewModel.helmetId
    val sortBy  = viewModel.sortBy
    val filteredIncident = viewModel.filteredIncidents(helmetId.value, sortBy.value, incidents)

    // Debug print to verify execution
    LaunchedEffect(Unit) {
        println("IncidentsScreen launched with ${incidents.size} incidents")
    }
    
    Column (modifier = Modifier.padding(12.dp)){
    Text(text = "Incidents",
        color = Color(0xFF47A0F6) ,
        fontSize = 30.sp,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(12.dp))
      SearchFilter(helmetId,sortBy)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredIncident) { incident ->
                IncidentItem(incident = incident)
            }

            // Show a placeholder if no incidents
            if (filteredIncident.isEmpty()) {
                item {
                    Text(
                        text = "No incidents recorded",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }


}

@Composable
fun IncidentItem(incident: Incident) {
    Log.d("search", "IncidentItem: $incident")

    // Define our custom colors
    val BlueAccent = Color(0xFF47A0F6)
    val DarkText = Color.Black
    val LightBlue = Color(0xFFE6F1FE)
    val MediumBlue = Color(0xFFADD8FF)

    // Determine severity color
    val severityColor = when (incident.getSeverityText()) {
        "Critical" -> Color(0xFFE57373) // Red for high severity
        "Severe" -> Color(0xFFFFD54F)
        "Serious" -> Color(0xFFFFD54F) // Amber for medium
        "Moderate"-> Color(0xFF81C784)
        "Minor"-> Color(0xFF81C784) // Green for low

        else -> Color.Gray // Default
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = BlueAccent.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top header bar with incident ID
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightBlue)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(BlueAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "#${incident.id}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Incident Report",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = incident.getFormattedTime(),
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkText.copy(alpha = 0.7f)
                        )
                    }
                }

                // Severity indicator
                Card(
                    colors = CardDefaults.cardColors(containerColor = severityColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = incident.getSeverityText(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Helmet ID with icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.helmet_filled),
                        contentDescription = "Helmet",
                        tint = BlueAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Helmet ID",
                            style = MaterialTheme.typography.labelMedium,
                            color = DarkText.copy(alpha = 0.6f)
                        )
                        Text(
                            text = incident.helmetId.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                    }
                }

                Divider(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Location with icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,  // Use appropriate icon
                        contentDescription = "Location",
                        tint = BlueAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.labelMedium,
                            color = DarkText.copy(alpha = 0.6f)
                        )

                        // Format coordinates to be more readable
                        val formattedLat = String.format("%.6f", incident.latitude)
                        val formattedLng = String.format("%.6f", incident.longitude)

                        Text(
                            text = "$formattedLat, $formattedLng",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DarkText
                        )
                    }
                }

                // Action buttons at the bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { /* View details action */ },
                        border = BorderStroke(1.dp, BlueAccent),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BlueAccent
                        )
                    ) {
                        Text("View Details")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { /* Share or export action */ },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueAccent
                        )
                    ) {
                        Text("Export")
                    }
                }
            }
        }
    }
}