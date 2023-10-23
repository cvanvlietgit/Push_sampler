package com.example.pushsampler

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.gms.tasks.OnCompleteListener
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pushsampler.ui.theme.PushSamplerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPermissionRequestContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPermissionRequestContent() {
    val PERMISSION_REQUEST_CODE = 123
    var permissionGranted by remember { mutableStateOf(false)}
    var fcmToken by remember { mutableStateOf("") }
    val TAG = "MainActivity"

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionGranted = true
        }
    }

    val getTokenButton = @Composable {
        Button(
            onClick = {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result
                        fcmToken = token ?: "Token not available"
                        Log.d(TAG, "FCM Token: $token")
                    } else {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        fcmToken = "Error fetching token"
                    }
                })
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Get FCM Token")
        }
    }


    PushSamplerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (permissionGranted) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Permission Granted")
                    getTokenButton()
                    TextField(
                        value = fcmToken,
                        onValueChange = { /* No action required */ },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        singleLine = true
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "This app needs permission to vibrate.")
                    getTokenButton()
                    // Use a TextField to display the FCM token, making it selectable and copyable
                    TextField(
                        value = fcmToken,
                        onValueChange = { /* No action required */ },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        singleLine = true
                    )
                }
            }
        }
    }
}
