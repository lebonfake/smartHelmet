package com.example.smarthelmet.model


data class Incident(
    var id: Int? = null,
    var helmet_id: Int? = null,
    var time: String? = null,
    var date: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
)
