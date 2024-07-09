package com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ShowPermissionDeniedDialog(showPermission: Boolean, onDismiss: () -> Unit, context: Context) {
    if (showPermission) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Permission Required") },
            text = { Text("Activity recognition permission is required for this feature. Please grant the permission in settings.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
            }
        )
    }
}