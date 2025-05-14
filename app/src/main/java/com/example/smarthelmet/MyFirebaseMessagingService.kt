package com.example.smarthelmet

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMsgService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "FROM: ${remoteMessage.from}")

        // Debug - print the entire message
        Log.d(TAG, "Message data: ${remoteMessage.data}")
        Log.d(TAG, "Message notification: ${remoteMessage.notification}")

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Notification Title: ${it.title}")
            Log.d(TAG, "Notification Body: ${it.body}")
            sendHeadsUpNotification(it.title ?: "Alert", it.body ?: "Check the app")
        }

        // Process data payload even if there's no notification
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // If there was no notification payload, create one from data
            if (remoteMessage.notification == null) {
                val title = remoteMessage.data["title"] ?: "Smart Helmet Alert"
                val body = remoteMessage.data["body"] ?:
                "Incident detected with severity ${remoteMessage.data["severity"] ?: "unknown"}"

                sendHeadsUpNotification(title, body)
            }
        }
    }

    private fun sendHeadsUpNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Add data for deep linking if needed
            putExtra("notification_source", "fcm")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "smart_helmet_alerts"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        
        // Create the NotificationChannel (for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Smart Helmet Alerts"
            val description = "Critical notifications for Smart Helmet incidents"
            // HIGH importance will make the notification pop up
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                this.description = description
                // Enable lights and vibration
                enableLights(true)
                enableVibration(true)
                // Set sound
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build()
                setSound(defaultSoundUri, audioAttributes)
                // Show on lock screen
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            
            // Register the channel with the system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification with HIGH priority (for pre-Android 8.0)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // You should create a dedicated notification icon
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // This is crucial for heads-up notification
            .setCategory(NotificationCompat.CATEGORY_ALARM) // Use ALARM for critical notifications
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 250, 500)) // Vibration pattern

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            try {
                // Use a unique ID based on timestamp for each notification
                val notificationId = System.currentTimeMillis().toInt()
                notify(notificationId, notificationBuilder.build())
            } catch (e: SecurityException) {
                // Handle the case where notification permission is not granted
                Log.e(TAG, "No permission to show notification: ${e.message}")
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // Send the new FCM token to your server to manage it
    }
}
