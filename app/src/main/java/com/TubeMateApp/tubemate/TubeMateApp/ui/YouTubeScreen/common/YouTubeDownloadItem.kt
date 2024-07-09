package com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.ui.theme.Orange

@Composable
fun YouTubeDownloadItem(
    thumbnailUrl: String,
    userName: String,
    downloadProgress: Int,
) {


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Image(
                painter = rememberImagePainter(data = thumbnailUrl),
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

            // Circular badge for number of posts
            /* Row(
                 modifier = Modifier
                     .padding(8.dp)
                     .align(Alignment.TopEnd)
                     .size(32.dp) // Ensure the size of the Row is explicitly set for circular shape
                     .background(
                         Color.Black,
                         CircleShape
                     ), // Use CircleShape for circular background
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.Center
             ) {
                 Text(
                     text = "$resolution", // Display number of posts
                     color = Color.White,
                     fontSize = 14.sp,
                     textAlign = TextAlign.Center,
                     modifier = Modifier.padding(8.dp)
                 )
             }*/
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${userName}", fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomProgressBar(
            progress = downloadProgress / 100f,
            backgroundColor = Color.LightGray,
            progressColor = Orange,
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(4.dp))
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CustomProgressBar(
    progress: Float,
    backgroundColor: Color = Color.LightGray,
    progressColor: Color = Color.Blue,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(4.dp)
            .background(backgroundColor, RoundedCornerShape(4.dp))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(progressColor, RoundedCornerShape(4.dp))
        ) {
        }
    }
}