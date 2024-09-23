package com.powervpn.PowerVPNApp.PowerVPN.ui.settings.AppUpdate

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.TubeMateApp.ui.SettingScreen.common.SettingScreenTopBar
import com.TubeMateApp.tubemate.ui.theme.statusBarColor
import com.powervpn.PowerVPNApp.PowerVPN.inAppUpdate.downloadApk
import com.powervpn.PowerVPNApp.PowerVPN.inAppUpdate.getAppVersion
import com.powervpn.PowerVPNApp.PowerVPN.inAppUpdate.getLatestVersionAndUrl
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUpdateScreen(navController: NavController){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isAvailable by remember { mutableStateOf(false) }
    var latestVersion by remember { mutableStateOf<String?>(null) }
    var latestUrl by remember { mutableStateOf<String?>(null) }
    val currentVersion = getAppVersion(context)
    var isFailedToCheckUpdates by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SettingScreenTopBar(
                title = "Update",
                onIconClick = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState
                        == Lifecycle.State.RESUMED
                    ) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.White.copy(.7f))
                .padding(bottom = 58.dp, start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Current version: $currentVersion",
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        scope.launch {
                            val (version, url) = getLatestVersionAndUrl("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") {
                                isFailedToCheckUpdates = it
                            }
                            latestVersion = version
                            latestUrl = url
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(Color.Black.copy(.1f))
                ) {
                    Text(
                        "Check for updates",
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }

                Spacer(Modifier.height(12.dp))

                if ((latestVersion ?: "") > currentVersion) {
                    Text(
                        text = "New update available: $latestVersion",
                        color = Color.Black,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            latestUrl?.let { downloadApk(context, it, latestVersion!!) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF268EEA)),
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    ) {
                        Text("Download", color = Color.Black, fontSize = 16.sp)
                    }

                } else if (currentVersion == latestVersion) {
                    Text(
                        text = "You have the latest version: $currentVersion",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                } else if (isFailedToCheckUpdates) {
                    Text(
                        text = "Error checking updates. Visit the link for manual updates.",
                        textAlign = TextAlign.Center,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Manually download the latest version:",
                    color = Color.Black.copy(1f),
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(8.dp))

                OpenLink() { isFailedToCheckUpdates = false }
            }
        }
    }
}

@Composable
fun OpenLink(onClick: () -> Unit) {
    val context = LocalContext.current
    Column {
        Text(
            text = "Click here",
            fontSize = 18.sp,
            textDecoration = TextDecoration.Underline,
            color = Color(0xFF2549A8),
            modifier = Modifier.clickable {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://app.mediafire.com/zvr02llqvcel9"))
                context.startActivity(intent)
                onClick()
            }
        )
    }
}