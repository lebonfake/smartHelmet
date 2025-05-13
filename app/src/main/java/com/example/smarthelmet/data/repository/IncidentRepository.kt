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
        Log.d("Firebase", "Connected to database: ${database.reference}")
    }

    /**
     * Adds an incident to Realtime Database with robust error handling.
     *
     * @param incident The incident object to store in the database
     * @param onSuccess Callback triggered when the operation completes successfully
     * @param onFailure Callback triggered when the operation fails, with exception details
     */
    fun addIncident(incident: Incident, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        try {
            // Format helmet_id with leading zeros (e.g., helmet_001)
            val helmetId = incident.helmetId ?: 0
            val formattedHelmetId = String.format("helmet_%03d", helmetId)

            // Generate incident ID if not present
            val incidentId = incident.id ?: (System.currentTimeMillis() % 1000).toInt()
            val formattedIncidentId = String.format("incident_%03d", incidentId)

            // Update the ID in the incident object
            incident.id = incidentId

            // Create the reference with the nested path
            val nestedRef = incidentsRef.child(formattedHelmetId).child(formattedIncidentId)

            nestedRef.setValue(incident)
                .addOnSuccessListener {
                    Log.d("RealtimeDB", "Added incident at: $formattedHelmetId/$formattedIncidentId")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    Log.e("RealtimeDB", "Failed to add incident: ${exception.message}", exception)
                    onFailure(exception)
                }
        } catch (e: Exception) {
            Log.e("RealtimeDB", "Unexpected error: ${e.message}", e)
            onFailure(e)
        }
    }

    /**
     * Registers a real-time listener for incidents data.
     * This will continuously notify the caller of any changes in the incidents.
     *
     * @param onData Callback function that receives the updated list of incidents
     * @param onError Callback function that receives any database errors
     * @return ValueEventListener to remove the listener when no longer needed
     */
    fun getIncidentsForHelmet(
        helmetId: Int,
        onData: (List<Incident>) -> Unit,
        onError: (Exception) -> Unit
    ): ValueEventListener {
        val formattedHelmetId = String.format("helmet_%03d", helmetId)
        Log.d("RealtimeDB", "Fetching incidents for $formattedHelmetId")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incidents = mutableListOf<Incident>()

                for (incidentSnapshot in snapshot.children) {
                    val incident = incidentSnapshot.getValue(Incident::class.java)
                    incident?.let { incidents.add(it) }
                }

                onData(incidents)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RealtimeDB", "Error fetching incidents: ${error.message}")
                onError(error.toException())
            }
        }

        incidentsRef.child(formattedHelmetId).addValueEventListener(listener)
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

                // First level: helmet_XXX nodes
                for (helmetSnapshot in snapshot.children) {
                    // Second level: incident_XXX nodes
                    for (incidentSnapshot in helmetSnapshot.children) {
                        val incident = incidentSnapshot.getValue(Incident::class.java)
                        incident?.let { incidents.add(it) }
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
