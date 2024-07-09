package com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.TubeMateApp.tubemate.ui.theme.Facebook
import com.TubeMateApp.tubemate.ui.theme.Instagram
import com.TubeMateApp.tubemate.ui.theme.Orange
import com.TubeMateApp.tubemate.ui.theme.Purple40
import com.TubeMateApp.tubemate.ui.theme.Purple80
import com.TubeMateApp.tubemate.ui.theme.WhatsApp

@Composable
fun WhatsAppDownloadSection(
    onDownloadClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Increased height to accommodate radio buttons
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(WhatsApp), // Instagram color
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*WhatsAppSearchBar(
            searchValue = searchValue,
            onValueChange = onValueChange,
            placeholderText = placeholderText
        )*/

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(16.dp)
                .size(50.dp)
                .clip(CircleShape)
                .border(3.dp, Purple40.copy(.3f), CircleShape),

            ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = Purple40.copy(.8f), // Adjust the icon color as needed
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = "Click on this button and allow permission to access your status",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = onDownloadClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 60.dp, end = 60.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Orange)
        ) {
            Text(
                text = "Allow storage access",
                color = Color.White, // Text color
                textAlign = TextAlign.Center,
            )
        }
    }
}

