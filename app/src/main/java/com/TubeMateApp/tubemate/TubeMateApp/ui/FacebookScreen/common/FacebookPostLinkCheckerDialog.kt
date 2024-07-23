package com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ehsanmsz.mszprogressindicator.progressindicator.BallTrianglePathProgressIndicator

@Composable
fun FacebookPostLinkCheckerDialog(isCheckingLink: Boolean) {
    if (isCheckingLink) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.Transparent,
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    BallTrianglePathProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        animationDuration = 800,
                        color = Color.Black,
                        strokeWidth = 1.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Please wait while we check the Instagram post link.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            },
            confirmButton = { },
            dismissButton = { }
        )
    }
}
