package com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeDownloadSection
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeDownloadStepsColumn
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeVideoLinkCheckerDialog
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeVideoSelectionBottomSheet
import com.TubeMateApp.tubemate.ui.theme.Instagram
import com.TubeMateApp.tubemate.ui.theme.YouTube
import java.io.File

@Composable
fun YouTubeScreen(navController: NavController, viewModel: TubeMateViewModel) {
    val SearchValue = remember { mutableStateOf("") }
    val selectedOption = remember { mutableStateOf("video") }
    val isYouTubeVideoFounded = viewModel.isYouTubeVideoFounded.collectAsState()
    val isAnythingDownloading = remember { mutableStateOf(false) }
    val downloadItems by viewModel.YouTubeDownloadItems.collectAsState()
    val YouTubeDownloadItemDetails by viewModel.YouTubeDownloadItemDetails.collectAsState()
    val YouTubeVideoSelectedOption = viewModel.YouTubeVideoSelectedOption.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            YouTubeScreenTopBar(
                title = "YouTube",
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
                        "https://www.youtube.com",
                        "com.google.android.youtube"
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

            YouTubeDownloadSection(
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
                    .background(YouTube)
                    .verticalScroll(scrollState)
            ) {

                downloadItems.forEach { item ->
                    YouTubeDownloadItem(
                        thumbnailUrl = item.thumbnail,
                        userName = item.title,
                        downloadProgress = item.downloadProgress
                    )
                }
                if (downloadItems.isEmpty()) {
                    YouTubeDownloadStepsColumn()
                }


                YouTubeVideoLinkCheckerDialog(isCheckingLink = isYouTubeVideoFounded.value)

                if (YouTubeVideoSelectedOption.value) {
                    YouTubeDownloadItemDetails.forEach { item ->
                        YouTubeVideoSelectionBottomSheet(
                            onDismiss = { viewModel.updateYouTubeVideoSelectedOption(false) },
                            videoThumbnailUrl = item.thumbnail,
                            videoTitle = item.title,
                            videoOptions = item.videoList,
                            audioOptions = item.audioList,
                            onVideoOptionSelected = { url ->
                                item.audioList.firstOrNull()?.let { audioInfo ->
                                    val audioUrl = audioInfo["url"] ?: "N/A"
                                    val urlList = listOf(url, audioUrl)
                                    viewModel.downloadYouTubeVideo(
                                        videoUrls = urlList,
                                        title = item.title,
                                        thumbnail = item.thumbnail,
                                        isYouTubeVideo = true,
                                        isYoutubeAudio = false,
                                    )
                                }
                                viewModel.updateYouTubeVideoSelectedOption(false)

                            },
                            onAudioOptionSelected = { url ->
                                val urlList = listOf(url)
                                viewModel.downloadYouTubeVideo(
                                    videoUrls = urlList,
                                    title = item.title,
                                    thumbnail = item.thumbnail,
                                    isYouTubeVideo = false,
                                    isYoutubeAudio = true,
                                )
                                viewModel.updateYouTubeVideoSelectedOption(false)

                            }
                        )
                    }
                }
            }
        }
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
    } else if (
        !searchValue.startsWith("https://www.youtube.com/") &&
        !searchValue.startsWith("https://youtu.be/") &&
        !searchValue.startsWith("https://youtube.com/")
    ) {
        Toast.makeText(context, "Invalid link", Toast.LENGTH_SHORT).show()
    } else {
        viewModel.fetchYouTubeVideoInfo(searchValue, context)
        onSuccess() // Invoke success callback
    }
}
