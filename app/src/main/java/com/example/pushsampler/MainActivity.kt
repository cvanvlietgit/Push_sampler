package com.example.pushsampler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.pushsampler.MainActivity.Companion.CHANNEL_ID
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent(this)
        }
        createNotificationChannel(this)
    }
    companion object {
        const val CHANNEL_ID = "channel_ID"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(activity: ComponentActivity) {
    var fcmToken by remember { mutableStateOf("") }
    val notificationManager = ContextCompat.getSystemService(activity, NotificationManager::class.java)

    // Fetch FCM token when the app starts
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            fcmToken = token ?: "Token not available"
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This app displays push notifications and local notifications.")

        // Button to show local notification
        Button(
            onClick = {
                val title = "Local Notification" //This is where you can change the name of your notification
                val content = "This is a local notification." //you can change the description of the notification here
                val notificationId = 1
                showNotification(activity, title, content, notificationManager, notificationId, CHANNEL_ID)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Show Local Notification")
        }

        // Text to display the FCM token
        TextField(
            value = fcmToken,
            onValueChange = { /* No action required */ },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            singleLine = true
        )
    }
}

fun showNotification(
    context: Context,
    title: String,
    content: String,
    notificationManager: NotificationManager?,
    notificationId: Int,
    channelID: String
) {
    if (notificationManager == null) {
        return
    }

    val builder = NotificationCompat.Builder(context, channelID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notificationManager.notify(notificationId, builder.build())
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Local notification Name" // Define the channel name
        val description = "this is a description for the notification channel" // Define the channel description
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}



