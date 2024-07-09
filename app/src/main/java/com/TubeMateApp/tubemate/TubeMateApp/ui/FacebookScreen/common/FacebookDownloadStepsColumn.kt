package com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.TubeMateApp.tubemate.R

@Composable
fun FacebookDownloadStepsColumn() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Step("Step 1 how to Download video", R.drawable.ic_launcher_background)
        Step("Step 2 how to Download video", R.drawable.ic_launcher_background)
        Step("Step 3 how to Download video", R.drawable.ic_launcher_background)
        Step("Step 4 how to Download video", R.drawable.ic_launcher_background)
    }
}

@Composable
fun Step(stepText: String, imageResId: Int) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(text = stepText, fontSize = 16.sp)
    Spacer(modifier = Modifier.height(8.dp))

    Image(
        painter = painterResource(id = imageResId),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )

}