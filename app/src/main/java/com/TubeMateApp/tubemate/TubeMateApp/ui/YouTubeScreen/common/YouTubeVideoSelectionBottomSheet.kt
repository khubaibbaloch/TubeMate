package com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes
import com.TubeMateApp.tubemate.ui.theme.YouTube


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeVideoSelectionBottomSheet(
    onDismiss: () -> Unit,
    videoThumbnailUrl: String,
    videoTitle: String,
    videoOptions: Map<String, Map<String, String>>,
    audioOptions: List<Map<String, String>>,
    onVideoOptionSelected: (String) -> Unit,
    onAudioOptionSelected: (String) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { },
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(YouTube)

        ) {
            BottomSheetContent(
                videoThumbnailUrl = videoThumbnailUrl,
                videoTitle = videoTitle,
                videoOptions = videoOptions,
                audioOptions = audioOptions,
                onVideoOptionSelected = onVideoOptionSelected, // Correctly passing the callback
                onAudioOptionSelected = onAudioOptionSelected // Correctly passing the callback
            )
        }

    }
}

@Composable
fun BottomSheetContent(
    videoThumbnailUrl: String,
    videoTitle: String,
    videoOptions: Map<String, Map<String, String>>,
    audioOptions: List<Map<String, String>>,
    onVideoOptionSelected: (String) -> Unit,
    onAudioOptionSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = rememberImagePainter(videoThumbnailUrl),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = videoTitle,
                fontSize = 14.sp,
                color = TubeMateThemes.colorScheme.textColor,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.padding(top = 8.dp).verticalScroll(rememberScrollState())) {
            val reversedVideoOptions = videoOptions.toList().asReversed()
            reversedVideoOptions.forEach { (resolution, data) ->

                val resolutionText = when (resolution) {
                    "144p" -> "SD"
                    "240p" -> "SD"
                    "360p" -> "SD"
                    "480p" -> "SD"
                    "720p" -> "HD"
                    "1080p" -> "HD"
                    else -> "Unknown"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVideoOptionSelected(data["url"] ?: "") }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = resolutionText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (resolutionText) {
                                "SD" -> Color.Yellow
                                "HD" -> Color.Red
                                else -> TubeMateThemes.colorScheme.textColor
                            }
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "$resolution", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))

                    val formattedSize = formatFileSize(data["size"])
                    Text(text = formattedSize, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            audioOptions.firstOrNull()?.let { audioInfo ->
                val audioUrl = audioInfo["url"] ?: "N/A"
                val format = audioInfo["format"] ?: "N/A"

                val formatLabel = when {
                    format.lowercase().contains("mp4a") -> "MP3"
                    "aac" in format.lowercase() -> "MP3"
                    "opus" in format.lowercase() -> "MP3"
                    "vorbis" in format.lowercase() -> "MP3"
                    format.isBlank() -> "MP3"
                    else -> format
                }
                val audioSize = when {
                    "M4A" in formatLabel -> "HD"
                    "MP3" in formatLabel -> "HD"
                    else -> "null"
                }
                val textColor = when (audioSize) {
                    "HD" -> Color.Red // HD for M4A
                    "SD" -> Color.Yellow // SD for MP3
                    else -> TubeMateThemes.colorScheme.textColor
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAudioOptionSelected(audioUrl) }
                        .padding(vertical = 8.dp)
                ) {

                    Text(
                        text = audioSize,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = formatLabel, style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.weight(1f))

                    Text(text = "5.5 MB", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


// Utility function to format file size in KB, MB, GB
fun formatFileSize(sizeStr: String?): String {
    sizeStr?.toDoubleOrNull()?.let { size ->
        val kb = size / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0

        return when {
            gb >= 1.0 -> String.format("%.2f GB", gb)
            mb >= 1.0 -> String.format("%.2f MB", mb)
            kb >= 1.0 -> String.format("%.2f KB", kb)
            else -> String.format("%.2f B", size)
        }
    }
    return "N/A"
}
