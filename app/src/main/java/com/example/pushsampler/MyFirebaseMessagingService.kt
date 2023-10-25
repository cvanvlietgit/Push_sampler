package com.example.pushsampler

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM message here
        val notificationData = remoteMessage.notification?.body
        if (notificationData != null) {
            // You can choose to process the FCM message here or just log it
            Log.d("FCM Message", notificationData)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM Token Refreshed", token)
        // You can send this token to your server or update it in your app as needed.
    }
}


