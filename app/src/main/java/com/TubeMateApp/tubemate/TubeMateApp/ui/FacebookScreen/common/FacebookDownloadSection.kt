package com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.TubeMateApp.tubemate.ui.theme.Facebook
import com.TubeMateApp.tubemate.ui.theme.Instagram
import com.TubeMateApp.tubemate.ui.theme.Orange
import com.TubeMateApp.tubemate.ui.theme.WhatsApp

@Composable
fun FacebookDownloadSection(
    searchValue: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    selectedOption: MutableState<String>,
    onDownloadClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Increased height to accommodate radio buttons
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Facebook), // Instagram color
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FacebookSearchBar(
            searchValue = searchValue,
            onValueChange = onValueChange,
            placeholderText = placeholderText
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
                text = "Download",
                color = Color.White, // Text color
                textAlign = TextAlign.Center,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Note: Downloading media from private accounts is not supported.",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            /* Row(
                 modifier = Modifier
                     .clip(RoundedCornerShape(8.dp))
                     .clickable { selectedOption.value = "video" }
                     .padding(8.dp),
                 horizontalArrangement = Arrangement.Center,
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 RadioButton(
                     selected = selectedOption.value == "video",
                     onClick = null, // Remove the separate click handler
                     colors = RadioButtonDefaults.colors(
                         selectedColor = Color.Black,
                         unselectedColor = Color.Gray
                     )
                 )
                 Text(text = "Video", modifier = Modifier.padding(end = 8.dp))
             }

             Spacer(modifier = Modifier.width(16.dp))

             Row(
                 modifier = Modifier
                     .clip(RoundedCornerShape(8.dp))
                     .clickable { selectedOption.value = "audio" }
                     .padding(8.dp),
                 horizontalArrangement = Arrangement.Center,
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 RadioButton(
                     selected = selectedOption.value == "audio",
                     onClick = null, // Remove the separate click handler
                     colors = RadioButtonDefaults.colors(
                         selectedColor = Color.Black,
                         unselectedColor = Color.Gray
                     )
                 )
                 Text(text = "Audio", modifier = Modifier.padding(end = 8.dp))
             }*/
        }
    }
}

