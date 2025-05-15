package com.example.smarthelmet.data.repository

import android.util.Log
import com.example.smarthelmet.model.Helmet
import com.google.firebase.database.FirebaseDatabase

class HelmetRepository {
    private val database = FirebaseDatabase.getInstance()
    private val helmetRef = database.getReference("helmets")

    init {
        Log.d("Firebase", "Connected to database URL: ${database.reference}")
        Log.d("Firebase", "Using reference path: $helmetRef")
    }

    fun getHelmets(onResult: (List<Helmet>) -> Unit, onError: (Exception) -> Unit) {
        helmetRef.addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val helmetList = mutableListOf<Helmet>()
                for (child in snapshot.children) {
                    var helmet = child.getValue(Helmet::class.java)
                    helmet?.helmetId = child.key

                    helmet?.let { helmetList.add(it) }
                }
                onResult(helmetList)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                onError(error.toException())
            }
        })
    }

}