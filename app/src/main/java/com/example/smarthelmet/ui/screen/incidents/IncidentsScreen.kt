package com.example.smarthelmet.ui.screen.incidents

import android.util.Log
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
    
    Column {
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
                text = "Helmet ID: ${incident.helmetId}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Time: ${incident.getFormattedTime()}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Location: ${incident.latitude}, ${incident.longitude}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Severity: ${incident.severity}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
