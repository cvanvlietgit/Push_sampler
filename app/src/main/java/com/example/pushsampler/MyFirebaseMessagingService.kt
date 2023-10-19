package com.example.pushsampler
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
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
    private val PERMISSION_REQUEST_CODE = 123
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var notification: NotificationCompat.Builder

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        notificationManager = NotificationManagerCompat.from(this)

        // Permission check
        if (ActivityCompat.checkSelfPermission(
                this,
                "android.permission.VIBRATE"
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            // Permission is already granted; proceed to create and show the notification
            showNotification(remoteMessage)
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf("android.permission.VIBRATE"),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        // Create a notification channel
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
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(0, notification.build())
    }
}

@Composable
fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This app needs permission to vibrate.")
        Button(onClick = onRequestPermission) {
            Text(text = "Request Permission")
        }
    }
}
