package com.example.smarthelmet.ui.screen.incidents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthelmet.model.Incident
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IncidentsScreen(viewModel: IncidentsViewModel = viewModel()) {
    val manualViewModel = IncidentsViewModel()
    val incidents by viewModel.incidents.collectAsState()

    // Debug print to verify execution
    LaunchedEffect(Unit) {
        println("IncidentsScreen launched with ${incidents.size} incidents")
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(incidents) { incident ->
            IncidentItem(incident = incident)
        }

        // Show a placeholder if no incidents
        if (incidents.isEmpty()) {
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

@Composable
fun IncidentItem(incident: Incident) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Incident #${incident.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Helmet ID: ${incident.helmet_id}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Format timestamp for better readability
            val formattedTime = try {
                val timestamp = incident.time?.toLongOrNull() ?: 0L
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(Date(timestamp))
            } catch (e: Exception) {
                incident.time
            }

            Text(
                text = "Time: $formattedTime",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Location: ${incident.latitude}, ${incident.longitude}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
