package com.example.smarthelmet.data.repository

import android.util.Log
import com.example.smarthelmet.model.Incident
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Repository class for handling Firebase Realtime Database operations related to incidents.
 * Provides methods for adding, retrieving, and monitoring incidents.
 */
class IncidentRepository {

    private val database = FirebaseDatabase.getInstance()
    private val incidentsRef = database.getReference("incidents")

    init {
        Log.d("Firebase", "Connected to database URL: ${database.reference}")
        Log.d("Firebase", "Using reference path: $incidentsRef")
    }

    /**
     * Registers a real-time listener for incidents data for a specific helmet.
     * This will continuously notify the caller of any changes in the incidents.
     *
     * @param helmetId The ID of the helmet to fetch incidents for
     * @param onData Callback function that receives the updated list of incidents
     * @param onError Callback function that receives any database errors
     * @return ValueEventListener to remove the listener when no longer needed
     */
    fun getIncidentsForHelmet(
        helmetId: Int,
        onData: (List<Incident>) -> Unit,
        onError: (Exception) -> Unit
    ): ValueEventListener {
        Log.d("RealtimeDB", "Fetching incidents for helmet ID: $helmetId")
        val helmetIdStr = helmetId.toString()

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incidents = mutableListOf<Incident>()

                for (incidentSnapshot in snapshot.children) {
                    try {
                        // Extract incident ID from key (incident_001 -> 1)
                        val incidentIdStr = incidentSnapshot.key?.replace("incident_", "") ?: ""
                        val incidentId = incidentIdStr.toIntOrNull()

                        // Get the incident
                        val incident = incidentSnapshot.getValue(Incident::class.java)

                        // Only include incidents for the requested helmet
                        if (incident?.helmetId == helmetIdStr) {
                            // Set the ID from the key if it's available
                            incident.id = incidentId

                            incidents.add(incident)
                            Log.d("RealtimeDB", "Found incident for helmet $helmetId: $incident")
                        }
                    } catch (e: Exception) {
                        Log.e("RealtimeDB", "Error processing incident: ${e.message}", e)
                    }
                }

                onData(incidents)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RealtimeDB", "Error fetching incidents: ${error.message}")
                onError(error.toException())
            }
        }

        incidentsRef.addValueEventListener(listener)
        return listener
    }

    /**
     * Fetches all incidents from the database.
     * This method retrieves all incidents across all helmets.
     *
     * @param onData Callback function that receives the list of incidents
     * @param onError Callback function that receives any database errors
     * @return ValueEventListener to remove the listener when no longer needed
     */
    fun getAllIncidents(
        onData: (List<Incident>) -> Unit,
        onError: (Exception) -> Unit
    ): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incidents = mutableListOf<Incident>()

                for (incidentSnapshot in snapshot.children) {
                    try {
                        // Extract incident ID from key (incident_001 -> 1)
                        val incidentIdStr = incidentSnapshot.key?.replace("incident_", "") ?: ""
                        val incidentId = incidentIdStr.toIntOrNull()

                        // Log raw data for debugging
                        Log.d("RealtimeDB", "Raw data for ${incidentSnapshot.key}: ${incidentSnapshot.value}")

                        // Get the incident and set ID from the key
                        val incident = incidentSnapshot.getValue(Incident::class.java)
                        incident?.let {
                            it.id = incidentId

                            Log.d("TimeDebug","Raw time value from DB: ${it.time}")
                            Log.d("TimeDebug","Parsed time value: ${it.getFormattedTime()}")

                            incidents.add(it)

                            // Log the incident after setting ID
                            Log.d("RealtimeDB", "Processed incident: $incident")
                        }
                    } catch (e: Exception) {
                        Log.e("RealtimeDB", "Error processing incident: ${e.message}", e)
                    }
                }

                onData(incidents)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RealtimeDB", "Error fetching all incidents: ${error.message}")
                onError(error.toException())
            }
        }

        incidentsRef.addValueEventListener(listener)
        return listener
    }

    /**
     * Removes a listener from the incidents reference.
     *
     * @param listener The ValueEventListener to remove
     */
    fun removeListener(listener: ValueEventListener) {
        incidentsRef.removeEventListener(listener)
    }
}
