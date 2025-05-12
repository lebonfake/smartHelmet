package com.example.smarthelmet.data.repository

import com.example.smarthelmet.model.Incident
import com.google.firebase.database.*

class IncidentRepository {

    private val databaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("incidents")



    // Function to get real-time updates on incidents
    fun getIncidents(onData: (List<Incident>) -> Unit, onError: (DatabaseError) -> Unit) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val incidents = snapshot.children.mapNotNull {
                    it.getValue(Incident::class.java)
                }
                onData(incidents)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    // Optional: Fetch incidents once (no listener)
    fun getIncidentsOnce(onData: (List<Incident>) -> Unit, onError: (DatabaseError) -> Unit) {
        databaseRef.get()
            .addOnSuccessListener { snapshot ->
                val incidents = snapshot.children.mapNotNull {
                    it.getValue(Incident::class.java)
                }
                onData(incidents)
            }
            .addOnFailureListener {
                onError(DatabaseError.fromException(it))
            }
    }
}
