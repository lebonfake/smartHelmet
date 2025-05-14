package com.example.smarthelmet.model

import android.util.Log
import com.google.firebase.database.PropertyName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Incident(
    var id: Int? = null,
    
    @get:PropertyName("helmet_id")
    @set:PropertyName("helmet_id")
    var helmetId: String? = null,

    @get:PropertyName("time")
    @set:PropertyName("time")
    var time: Long? = null,

    @get:PropertyName("latitude")
    @set:PropertyName("latitude")
    var latitude: Double? = null,

    @get:PropertyName("longitude")
    @set:PropertyName("longitude")
    var longitude: Double? = null,

    @get:PropertyName("severity")
    @set:PropertyName("severity")
    var severity: Int? = null // 1-5 Severity scale (1 = low, 5 = extreme)
) {
    // Helper method to get a nicely formatted time string
    fun getFormattedTime(): String {
        if (time == null) return "Unknown time"

        return try {
            // Convert Unix time (seconds) to milliseconds for Date
            val date = Date(time!! * 1000)
            
            // Format the date
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            Log.e("Incident", "Error formatting Unix timestamp: $time - ${e.message}")
            "Error: $time"
        }
    }
    
    /**
     * Returns the severity level as a user-friendly string
     */
    fun getSeverityText(): String {
        return when (severity) {
            1 -> "Minor"
            2 -> "Moderate" 
            3 -> "Serious"
            4 -> "Severe"
            5 -> "Critical"
            else -> "Unknown"
        }
    }
    
    /**
     * Returns a relative time description (e.g., "5 minutes ago")
     */
    fun getRelativeTime(): String {
        if (time == null) return "Unknown time"
        
        try {
            val now = System.currentTimeMillis() / 1000 // Current Unix time in seconds
            val diff = now - time!! // Difference in seconds
            
            return when {
                diff < 60 -> "Just now"
                diff < 60 * 60 -> "${diff / 60} minutes ago"
                diff < 24 * 60 * 60 -> "${diff / (60 * 60)} hours ago"
                diff < 48 * 60 * 60 -> "Yesterday"
                else -> "${diff / (24 * 60 * 60)} days ago"
            }
        } catch (e: Exception) {
            Log.e("Incident", "Error calculating relative time: $time - ${e.message}")
            return "Unknown time"
        }
    }
}
