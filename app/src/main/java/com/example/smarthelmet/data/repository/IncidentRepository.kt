package com.example.smarthelmet.data.repository

import android.util.Log
import com.example.smarthelmet.model.Incident
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject

/**
 * Repository class for handling Firestore operations related to incidents.
 * Provides methods for adding, retrieving, and monitoring incidents in Firestore.
 */
class IncidentRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val incidentsCollection = firestore.collection("incidents")

    /**
     * Adds an incident to Firestore with robust error handling.
     *
     * @param incident The incident object to store in the database
     * @param onSuccess Callback triggered when the operation completes successfully
     * @param onFailure Callback triggered when the operation fails, with exception details
     */
    fun addIncident(incident: Incident, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        try {
            val newDocRef = incidentsCollection.document()
            newDocRef.set(incident)
                .addOnSuccessListener {
                    Log.d("Firestore", "Successfully added incident with ID: ${newDocRef.id}")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Failed to add incident: ${exception.message}", exception)
                    onFailure(exception)
                }
        } catch (e: Exception) {
            Log.e("Firestore", "Unexpected error: ${e.message}", e)
            onFailure(e)
        }
    }

    /**
     * Registers a real-time listener for incidents data.
     * This will continuously notify the caller of any changes in the incidents collection.
     *
     * @param onData Callback function that receives the updated list of incidents
     * @param onError Callback function that receives any database errors
     * @return ListenerRegistration to remove the listener when no longer needed
     */
    fun getIncidents(
        onData: (List<Incident>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        Log.d("Firestore", "Setting up real-time listener")
        return incidentsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("Firestore", "Incident listener error: ${exception.message}", exception)
                onError(exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val incidents = snapshot.documents.mapNotNull { it.toObject<Incident>() }
                onData(incidents)
            }
        }
    }

    /**
     * Fetches incidents data once without setting up a continuous listener.
     * Useful for one-time data needs.
     *
     * @param onData Callback function that receives the list of incidents
     * @param onError Callback function that receives any database errors
     */
    fun getIncidentsOnce(onData: (List<Incident>) -> Unit, onError: (Exception) -> Unit) {
        incidentsCollection.get()
            .addOnSuccessListener { snapshot ->
                val incidents = snapshot.documents.mapNotNull { it.toObject<Incident>() }
                onData(incidents)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Failed to fetch incidents: ${exception.message}", exception)
                onError(exception)
            }
    }
}
