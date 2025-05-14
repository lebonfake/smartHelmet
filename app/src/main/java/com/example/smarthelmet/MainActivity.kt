package com.example.smarthelmet

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smarthelmet.ui.AppNavHost
import com.google.firebase.FirebaseApp
import android.Manifest
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Obtain the FCM registration token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed: ${task.exception}")
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            println("FCM Registration Token: $token")

            // Save the token to your database
            saveTokenToDatabase(token)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        setContent {
            MaterialTheme{
                AppNavHost()
            }
        }
    }
}

private fun saveTokenToDatabase(token: String) {
    // Save to Firebase Database so your ESP8266 can retrieve it
    FirebaseDatabase.getInstance().getReference("fcm_tokens")
        .child("my_device")
        .setValue(token)
        .addOnSuccessListener {
            Log.d("FCM", "Token saved to database")
        }
        .addOnFailureListener { e ->
            Log.e("FCM", "Failed to save token: ${e.message}")
        }
}