package com.example.smarthelmet.model

data class Worker(
    var id: String? = null,
    var fullName: String = "", // Provide default values
    var age: Int = 0,             // for all properties
    var healthState: String = ""
) {
    constructor() : this(null, "", 0, "") // Add a no-argument constructor
}