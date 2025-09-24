package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.composables

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.permissions.NotificationPermissionHandler


@Composable
fun NotificationPermissionGate(
    notificationHandler: NotificationPermissionHandler,
    onGranted: () -> Unit,
    autoRequestOnStart: Boolean = true,
    contentWhenDenied: @Composable (request: () -> Unit) -> Unit = { request ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("We use notifications to show push messages from the workshop.")
            Spacer(Modifier.height(12.dp))
            Button(onClick = request) { Text("Allow notifications") }
        }
    }
) {
    val context = LocalContext.current

    // ActivityResult launcher lives safely across recompositions
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) onGranted()
    }

    // One-shot auto request control
    var requestedOnce by remember { mutableStateOf(false) }

    val needsPermission =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                notificationHandler.isPermissionRequired() &&
                !notificationHandler.isGranted(context)

    LaunchedEffect(needsPermission, autoRequestOnStart) {
        if (needsPermission && autoRequestOnStart && !requestedOnce) {
            requestedOnce = true
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else if (!needsPermission) {
            // Already granted or not required
            onGranted()
        }
    }

    if (needsPermission) {
        contentWhenDenied { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }
    } else {
        // Permission granted / not required: render nothing here.
        // onGranted() will have been called; parent can show main UI.
        Spacer(Modifier.size(0.dp))
    }
}
