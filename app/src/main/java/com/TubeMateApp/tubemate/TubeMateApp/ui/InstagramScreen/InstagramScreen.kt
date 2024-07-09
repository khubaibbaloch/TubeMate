package com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadSection
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadStepsColumn
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramPostLinkCheckerDialog
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramScreenTopBar
import com.TubeMateApp.tubemate.ui.theme.Instagram
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.ehsanmsz.mszprogressindicator.progressindicator.BallClipRotateProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallClipRotatePulseProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallGridPulseProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallPulseProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallPulseRiseProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallRotateProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallScaleRippleMultipleProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallTrianglePathProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagDeflectProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.BallZigZagProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.CubeTransitionProgressIndicator
import com.ehsanmsz.mszprogressindicator.progressindicator.SquareSpinProgressIndicator
import java.util.concurrent.Callable


@Composable
fun InstagramScreen(navController: NavController, viewModel: TubeMateViewModel) {
    var SearchValue = remember { mutableStateOf("") }
    val selectedOption = remember { mutableStateOf("video") }
    val isInstagramPostFounded = viewModel.isInstagramPostFounded.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val isAnythingDownloading = remember { mutableStateOf(false) }
    val downloadItems by viewModel.instagramDownloadItems.collectAsState()

    Scaffold(
        topBar = {
            InstagramScreenTopBar(
                title = "Instagram",
                onIconClick = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState
                        == Lifecycle.State.RESUMED
                    ) {
                        navController.popBackStack()
                    }
                },
                onAppClick = {
                    /*val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://www.youtube.com")
                        setPackage("com.google.android.youtube")
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        startActivity(context, intent, null)
                    } else {
                        // YouTube app is not installed, open in browser
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                        startActivity(context, browserIntent, null)
                    }*/

                    viewModel.openAppOrBrowser(
                        context,
                        "https://www.instagram.com",
                        "com.instagram.android"
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
            InstagramDownloadSection(
                searchValue = SearchValue.value,
                onValueChange = { newValue -> SearchValue.value = newValue },
                placeholderText = "Paste link here",
                selectedOption = selectedOption,
                onDownloadClick = {
                    handleDownloadClick(
                        searchValue = SearchValue.value,
                        context = context,
                        viewModel = viewModel
                    ) {
                        SearchValue.value = ""
                        isAnythingDownloading.value = true
                    }
                }
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Instagram)
                    .verticalScroll(scrollState)
            ) {
                downloadItems.forEach { item ->
                    InstagramDownloadItem(
                        thumbnailUrl = item.postUrl,
                        userName = item.userName,
                        numberOfPosts = item.postSize,
                        downloadProgress = item.downloadProgress
                    )
                }
                if (downloadItems.isEmpty()) {
                    InstagramDownloadStepsColumn()
                }
            }
        }

        InstagramPostLinkCheckerDialog(isInstagramPostFounded.value)
    }

}

fun handleDownloadClick(
    searchValue: String,
    context: Context,
    viewModel: TubeMateViewModel,
    onSuccess: () -> Unit, // Add a callback for success handling
) {
    if (searchValue.isEmpty()) {
        Toast.makeText(context, "Please paste the link", Toast.LENGTH_SHORT).show()
    } else if (!searchValue.startsWith("https://www.instagram.com/")) {
        Toast.makeText(context, "Invalid link", Toast.LENGTH_SHORT).show()
    } else {
        viewModel.fetchInstagramInfo(searchValue, context)
        onSuccess() // Invoke success callback
    }
}