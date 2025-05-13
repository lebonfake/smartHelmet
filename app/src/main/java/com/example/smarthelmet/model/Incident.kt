package com.example.smarthelmet.model


data class Incident(
    var id: Int? = null,
    var helmetId: Int? = null,
    var time: String? = null,
    var date: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var severity: Int? = null
)
