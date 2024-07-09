package com.TubeMateApp.tubemate

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.RootNavGraph
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TubeMateViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TubeMateThemes(2, this@MainActivity) {
                val navController = rememberNavController()
                RootNavGraph(navController = navController, viewModel = viewModel)
                checkPermissionAndRequest(navController, this@MainActivity)
                // "context" must be an Activity, Service or Application object from your app.
                /* if (! Python.isStarted()) {
                     Python.start(AndroidPlatform(this@MainActivity));
                 }*/

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun checkPermissionAndRequest(navController: NavController, activity: ComponentActivity) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        if (currentRoute == "HomeScreen") {
            val missingPermissions = mutableListOf<String>()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    missingPermissions.add(Manifest.permission.READ_MEDIA_IMAGES)
                }

                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    missingPermissions.add(Manifest.permission.READ_MEDIA_VIDEO)
                }

                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_MEDIA_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    missingPermissions.add(Manifest.permission.READ_MEDIA_AUDIO)
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    missingPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }

            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }

            if (missingPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    missingPermissions.toTypedArray(),
                    REQUEST_CODE_STORAGE_PERMISSIONS
                )
            } else {
                // Permissions are already granted, do something if needed
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSIONS = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val takeFlags: Int =
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                contentResolver.takePersistableUriPermission(uri, takeFlags)

                val expectedUri2 =
                    Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses")

                if (uri == expectedUri2) {
                    // Save the Uri for future access
                    viewModel.saveUri(uri)
                    Log.d("TAG", "onActivityResult: $uri")
                } else {
                    // Notify the user about wrong permission granted
                    Toast.makeText(this, "Wrong permission granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

