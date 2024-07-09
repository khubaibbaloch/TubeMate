package com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen

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
import com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common.FacebookScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadItem
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadSection
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramDownloadStepsColumn
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramScreenTopBar
import com.TubeMateApp.tubemate.ui.theme.Facebook
import com.TubeMateApp.tubemate.ui.theme.Instagram

@Composable
fun FacebookScreen(navController: NavController, viewModel: TubeMateViewModel) {
    val SearchValue = remember { mutableStateOf("") }
    val selectedOption = remember { mutableStateOf("video") }
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
                onDownloadClick = { viewModel.fetchFacebookInfo(context, SearchValue.value) }
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Facebook)
                    .verticalScroll(scrollState)
            ) {

                FacebookDownloadItem()
                FacebookDownloadStepsColumn()


            }
        }
    }
}

