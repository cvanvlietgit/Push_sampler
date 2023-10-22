package com.example.pushsampler
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var notification: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()

        // Initialize the notificationManager
        notificationManager = NotificationManagerCompat.from(this)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if the notification payload contains the message to show
        // Extract the notification message from the RemoteMessage
        val notificationData = remoteMessage.notification?.body

        if (notificationData != null) {
            // Check for permission to vibrate
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                showNotification(notificationData)
            } else {
                val intent = Intent(this, PermissionRequestActivity::class.java)
                intent.putExtra("message", notificationData)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM Token Refreshed", token)
        // You can send this token to your server or update it in your app as needed.
    }


    private fun showNotification(notificationData: String) {
        // Check for the VIBRATE permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.VIBRATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted, proceed to create and show the notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "default",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            notification = NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Notification Title") // Set your title here
                .setContentText(notificationData)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            notificationManager.notify(0, notification.build())
        } else {
            // Permission is not granted, you should request it here or handle it as needed
        }
    }
}
