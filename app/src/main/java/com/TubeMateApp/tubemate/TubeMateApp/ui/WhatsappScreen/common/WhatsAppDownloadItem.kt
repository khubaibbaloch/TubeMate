package com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen.common

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.ui.theme.Purple40
import com.TubeMateApp.tubemate.ui.theme.PurpleGrey80
import com.bumptech.glide.Glide
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun WhatsAppDownloadItem(mediaUri: Uri?, onDownloadClick: (Uri) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        mediaUri?.let { uri ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val context = LocalContext.current
                val mimeType = context.contentResolver.getType(uri)

                // Check file extension if MIME type is not available
                val fileName = uri.lastPathSegment ?: ""
                val isImageFile = fileName.endsWith(".jpg", ignoreCase = true)
                val isVideoFile = fileName.endsWith(".mp4", ignoreCase = true)

                when {
                    mimeType?.startsWith("image/") == true || isImageFile -> {
                        GlideImage(
                            imageModel = { uri },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    mimeType?.startsWith("video/") == true || isVideoFile -> {
                        Box {
                            GlideImage(
                                imageModel = { uri },
                                requestBuilder = {
                                    Glide.with(context)
                                        .asBitmap()
                                        .load(uri)
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
                    else -> {
                        // Handle unsupported MIME types or other cases
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable { onDownloadClick(mediaUri) }, // Invoke the onClick lambda here
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "download",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}
