package com.TubeMateApp.tubemate.TubeMateApp.ui.HistoryScreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.HistoryScreen.common.HistoryScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.common.HomeScreenTopBar
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.BottomNavScreenRoutes
import com.bumptech.glide.Glide
import com.skydoves.landscapist.glide.GlideImage
import java.io.File

@Composable
fun HistoryScreen(navController: NavController, viewModel: TubeMateViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val mediaFiles = viewModel.mediaFiles.collectAsState().value


    LaunchedEffect(Unit) {
        viewModel.loadMediaFiles(context)
    }

    Scaffold(
        topBar = {
            HistoryScreenTopBar(
                title = "History",
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
            LazyColumn(
            ) {
                items(mediaFiles) { file ->
                    ItemColumn(uri = file.uri, title = file.name, size = file.size)
                }
            }
        }

    }
}


@Composable
fun ItemColumn(uri: Uri, title: String, size: Long) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(uri)
    val fileName = uri.lastPathSegment ?: ""

    val isImageFile = mimeType?.startsWith("image/") ?: fileName.endsWith(".jpg", ignoreCase = true)
    val isVideoFile = mimeType?.startsWith("video/") ?: fileName.endsWith(".mp4", ignoreCase = true)
    val isAudioFile = mimeType?.startsWith("audio/") ?: fileName.endsWith(".mp3", ignoreCase = true) ||
            fileName.endsWith(".wav", ignoreCase = true) ||
            fileName.endsWith(".ogg", ignoreCase = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(.3f))
            ) {
                when {
                    isImageFile -> {
                        GlideImage(
                            imageModel = { uri },
                            previewPlaceholder = painterResource(id = R.drawable.image_icon),
                            modifier = Modifier.fillMaxSize(),
                        )
                        Log.d("TAG", "isImageFile: $uri")
                    }

                    isVideoFile -> {
                        GlideImage(
                            imageModel = { uri },
                            previewPlaceholder = painterResource(id = R.drawable.video_icon),
                            modifier = Modifier.fillMaxSize(),
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.video_play_icon),
                            contentDescription = "Play Video",
                            tint = Color.White,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                                .padding(4.dp)
                        )
                        Log.d("TAG", "isVideoFile: $uri")
                    }

                    isAudioFile -> {
                        GlideImage(
                            imageModel = { },
                            previewPlaceholder = painterResource(id = R.drawable.audio_icon),
                            modifier = Modifier.fillMaxSize(),
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.audio_icon),
                            contentDescription = "Play Video",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                                .padding(4.dp)
                        )
                    }

                    else -> {
                        // Handle unsupported file types
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${size} bytes",
                    color = Color.Gray
                )
            }
        }
    }
}


/*@Composable
fun MediaItem(file: Uri) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val context = LocalContext.current
        val mimeType = context.contentResolver.getType(file)

        // Check file extension if MIME type is not available
        val fileName = file.lastPathSegment ?: ""
        val isImageFile = fileName.endsWith(".jpg", ignoreCase = true)
        val isVideoFile = fileName.endsWith(".mp4", ignoreCase = true)
        val isAudioFile = fileName.endsWith(".mp3", ignoreCase = true) ||
                fileName.endsWith(".wav", ignoreCase = true) ||
                fileName.endsWith(".ogg", ignoreCase = true)

        when {
            mimeType?.startsWith("image/") == true || isImageFile -> {
                GlideImage(
                    imageModel = { file },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            mimeType?.startsWith("video/") == true || isVideoFile -> {
                Box {
                    GlideImage(
                        imageModel = { file },
                        requestBuilder = {
                            Glide.with(context)
                                .asBitmap()
                                .load(file)
                                .frame(1000000) // Request frame at 1-second mark
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.video_play_icon),
                        contentDescription = "Play Video",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            mimeType?.startsWith("audio/") == true || isAudioFile -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.video_play_icon),
                        contentDescription = "Audio File",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = fileName, modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            else -> {
                // Handle unsupported MIME types or other cases
            }
        }
    }
}*/