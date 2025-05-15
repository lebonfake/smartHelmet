package com.example.smarthelmet.model

import com.google.firebase.database.PropertyName

data class Helmet (

    @get:PropertyName("helmet_id")
    @set:PropertyName("helmet_id")
     var helmetId :String? = null

){

}
