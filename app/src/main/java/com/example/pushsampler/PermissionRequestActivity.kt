package com.example.pushsampler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.pushsampler.ui.theme.PushSamplerTheme
import androidx.activity.compose.rememberLauncherForActivityResult

class PermissionRequestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionRequestContent()
        }
    }
}

@Composable
fun PermissionRequestContent() {
    var permissionGranted by remember { mutableStateOf(false)}


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionGranted = true
        }
    }

    PushSamplerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (permissionGranted) {
                // Permission granted
                PermissionGrantedScreen()
            } else {
                // Permission not granted
                PermissionRequestScreen{
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }
    }
}


@Composable
fun PermissionGrantedScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Permission Granted")
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
        Button(onClick = onRequestPermission) { // Use the lambda directly
            Text(text = "Request Permission")
        }
    }
}

