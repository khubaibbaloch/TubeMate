package com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadSection
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadStepsColumn
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen.common.WhatsAppDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen.common.WhatsAppDownloadSection
import com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen.common.WhatsAppDownloadStepsColumn
import com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen.common.WhatsAppScreenTopBar
import com.TubeMateApp.tubemate.ui.theme.Instagram
import com.TubeMateApp.tubemate.ui.theme.WhatsApp
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun WhatsAppScreen(navController: NavController, viewModel: TubeMateViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val isUriPermissionGranted = viewModel.uriPermissionGranted.collectAsState()


    Scaffold(
        topBar = {
            WhatsAppScreenTopBar(
                title = "WhatsApp",
                onIconClick = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState
                        == Lifecycle.State.RESUMED
                    ) {
                        navController.popBackStack()
                    }
                },
                onAppClick = {
                    viewModel.openAppOrBrowser(
                        context,
                        "https://www.whatsapp.com",
                        "com.whatsapp"
                    )
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
            when {
                isUriPermissionGranted.value == true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    WhatsAppContent(context = context, viewModel = viewModel)
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> {
                    WhatsAppContent(context = context, viewModel = viewModel)
                }

                else -> {
                    WhatsAppDownloadSection(
                        onDownloadClick = {
                            viewModel.requestPermissionQ(context)
                        }
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(WhatsApp)
                            .verticalScroll(scrollState)
                    ) {
                        WhatsAppDownloadStepsColumn()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun WhatsAppContent(context: Context, viewModel: TubeMateViewModel) {
    val statusUris = viewModel.getWhatsAppStatuses(context)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsApp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (statusUris.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp)
            ) {
                items(statusUris.size) { index ->
                    WhatsAppDownloadItem(
                        mediaUri = statusUris[index],
                        onDownloadClick = { uri ->
                            coroutineScope.launch {
                                val fileName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    val extension = viewModel.getFileExtension(context, uri)
                                    val extensionTypes = viewModel.getExtensionTypes(context, uri)
                                    Log.d("TAG", "WhatsAppContent: $extension")
                                    "TubeMate.${extensionTypes}_${System.currentTimeMillis()}.$extension"
                                } else {
                                    val extension = viewModel.getFileExtension(context, uri)

                                    uri.lastPathSegment
                                        ?: "TubeMate.${extension}_${System.currentTimeMillis()}"
                                }

                                val success = viewModel.copyFileToDownloads(
                                    context = context,
                                    uri = uri,
                                    fileName = fileName
                                )
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "File Downloaded successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(
                                        "WhatsAppScreen",
                                        "File copied successfully to Downloads: $fileName"
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to download file",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e(
                                        "WhatsAppScreen",
                                        "Failed to copy file to Downloads: $fileName"
                                    )
                                }
                            }
                        }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No WhatsApp statuses found.",
                    modifier = Modifier.padding(16.dp)
                )
            }

        }
    }
}
