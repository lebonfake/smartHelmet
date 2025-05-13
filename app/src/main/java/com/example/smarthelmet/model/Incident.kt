package com.example.smarthelmet.model

import android.util.Log
import com.google.firebase.database.PropertyName

data class Incident(
    var id: Int? = null,
    
    @get:PropertyName("helmet_id")
    @set:PropertyName("helmet_id")
    var helmetId: String? = null,

    @get:PropertyName("time")
    @set:PropertyName("time")
    var time: String? = null,

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
        if (time.isNullOrEmpty()) return "Unknown time"

        return try {
            // First try parsing as a long timestamp (milliseconds since epoch)
            val timestampLong = time!!.toLongOrNull()
            if (timestampLong != null) {
                java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                    .format(java.util.Date(timestampLong))
            }
            // Next try parsing as ISO 8601 format (2023-05-15T10:30:00Z)
            else if (time!!.contains("T") && (time!!.contains("Z") || time!!.contains("+"))) {
                val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")

                val parsedDate = inputFormat.parse(time!!)
                val outputFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                outputFormat.timeZone = java.util.TimeZone.getDefault() // Convert to local time zone

                parsedDate?.let { outputFormat.format(it) } ?: "Invalid date"
            }
            // If neither works, return the raw string
            else {
                time!!
            }
        } catch (e: Exception) {
            Log.e("Incident", "Error parsing time: ${time} - ${e.message}")
            "Error: ${time}"
        }
    }
}
