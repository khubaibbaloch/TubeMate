package com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common.FacebookDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common.FacebookDownloadSection
import com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common.FacebookDownloadStepsColumn
import com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common.FacebookPostLinkCheckerDialog
import com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common.FacebookScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadSection
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadStepsColumn
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common.YouTubeDownloadStepsColumn
import com.TubeMateApp.tubemate.ui.theme.Facebook
import com.TubeMateApp.tubemate.ui.theme.Instagram

@Composable
fun FacebookScreen(navController: NavController, viewModel: TubeMateViewModel) {
    val SearchValue = remember { mutableStateOf("") }
    val selectedOption = remember { mutableStateOf("video") }
    val downloadItems by viewModel.FacebookDownloadItems.collectAsState()
    val isFacebookPostFounded = viewModel.isFacebookVideoFounded.collectAsState()
    val isAnythingDownloading = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            FacebookScreenTopBar(
                title = "Facebook",
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
                        "https://www.facebook.com",
                        "com.facebook.katana"
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

            FacebookDownloadSection(
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
                    .background(Facebook)
                    .verticalScroll(scrollState)
            ) {

                downloadItems.forEach { item ->
                    FacebookDownloadItem(
                        context = context,
                        thumbnailUrl = item.thumbnail,
                        userName = item.title,
                        downloadProgress = item.downloadProgress
                    )
                }
                if (downloadItems.isEmpty()) {
                    FacebookDownloadStepsColumn()
                }

            }
            FacebookPostLinkCheckerDialog(isFacebookPostFounded.value)
        }
    }
}

fun handleDownloadClick(
    searchValue: String,
    context: Context,
    viewModel: TubeMateViewModel,
    onSuccess: () -> Unit,
) {
    if (searchValue.isEmpty()) {
        Toast.makeText(context, "Please paste the link", Toast.LENGTH_SHORT).show()
    } else if (!searchValue.startsWith("https://www.facebook.com/")) {
        Toast.makeText(context, "Invalid link", Toast.LENGTH_SHORT).show()
    } else {
        viewModel.fetchFacebookInfo(context,searchValue )
        onSuccess() // Invoke success callback
    }
}