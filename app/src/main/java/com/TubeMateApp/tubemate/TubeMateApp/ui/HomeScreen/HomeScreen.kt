package com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.common.HomeScreenItemCard
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.common.HomeScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.common.ShowPermissionDeniedDialog
import com.TubeMateApp.tubemate.TubeMateApp.ui.MainScreen.common.CustomBottomNav
import com.TubeMateApp.tubemate.ui.theme.Facebook
import com.TubeMateApp.tubemate.ui.theme.Instagram
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes
import com.TubeMateApp.tubemate.ui.theme.WhatsApp
import com.TubeMateApp.tubemate.ui.theme.YouTube
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.BottomNavScreenRoutes

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }



    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            val isGranted = it.value
            if (!isGranted) {
                showPermissionDeniedDialog = true
            }
        }
    }

    val checkPermissions: () -> List<String> = {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }

            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
            }

            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionsToRequest
    }

    LaunchedEffect(Unit) {
        val permissionsToRequest = checkPermissions()
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    val checkPermissionsAndNavigate: (String) -> Unit = { route ->
        val permissionsToRequest = checkPermissions()
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            navController.navigate(route)
        }
    }

    Scaffold(
        topBar = {
            HomeScreenTopBar(
                title = "TubeMate",
                onIconClick = {
                    navController.navigate(BottomNavScreenRoutes.SettingScreen.route)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomeScreenItemCard(
                    imageResId = R.drawable.social,
                    mainText = "Instagram",
                    backgroundColor = Instagram,
                    onClick = {

                        checkPermissionsAndNavigate(BottomNavScreenRoutes.InstagramScreen.route)
                    }
                )

                HomeScreenItemCard(
                    imageResId = R.drawable.whatsapp,
                    mainText = "WhatsApp",
                    backgroundColor = WhatsApp,
                    onClick = {
                        checkPermissionsAndNavigate(BottomNavScreenRoutes.WhatsAppScreen.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomeScreenItemCard(
                    imageResId = R.drawable.facebook,
                    mainText = "Facebook",
                    backgroundColor = Facebook,
                    onClick = {
                        checkPermissionsAndNavigate(BottomNavScreenRoutes.FacebookScreen.route)
                    }
                )
                HomeScreenItemCard(
                    imageResId = R.drawable.youtube,
                    mainText = "YouTube",
                    backgroundColor = YouTube,
                    onClick = {
                        checkPermissionsAndNavigate(BottomNavScreenRoutes.YouTubeScreen.route)
                    }
                )
            }

            ShowPermissionDeniedDialog(
                showPermission = showPermissionDeniedDialog,
                onDismiss = { showPermissionDeniedDialog = false },
                context = context
            )
        }
    }
}
