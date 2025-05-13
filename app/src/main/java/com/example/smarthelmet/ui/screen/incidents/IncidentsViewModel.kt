
package com.example.smarthelmet.ui.screen.incidents
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthelmet.data.repository.IncidentRepository
import com.example.smarthelmet.model.Incident
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IncidentsViewModel(
    private val repository: IncidentRepository = IncidentRepository()
) : ViewModel() {

    private val _incidents = MutableStateFlow<List<Incident>>(emptyList())
    val incidents: StateFlow<List<Incident>> = _incidents

    init {
        Log.d("IncidentsViewModel", "ViewModel initialized")
        loadIncidents()
        //addTestIncident() // For testing
    }

    private fun addTestIncident() {
        viewModelScope.launch {
            // Create a test Incident instance
            val testIncident = Incident(
                id = 1,
                helmet_id = 1,
                time = System.currentTimeMillis().toString(),
                date = System.currentTimeMillis().toString(),
                latitude = 12.0,
                longitude = 12.0
            )

            // Log before adding
            Log.d("IncidentsViewModel", "Adding test incident: $testIncident")

            repository.addIncident(
                incident = testIncident,
                onSuccess = {
                    Log.d("IncidentsViewModel", "Incident added successfully")

                },
                onFailure = { e ->
                    Log.e("IncidentsViewModel", "Error adding incident: ${e.message}")
                }
            )
        }
    }

    private fun loadIncidents() {
        viewModelScope.launch {
            Log.d("IncidentsViewModel", "Fetching incidents from repository")

            repository.getIncidents(
                onData = { list ->
                    _incidents.value = list
                    Log.d("IncidentsViewModel", "Fetched incidents successfully: ${list.size} items")
                },
                onError = { err ->
                    Log.e("IncidentsViewModel", "Error fetching incidents: ${err.message}")
                }
            )
        }
    }
}
