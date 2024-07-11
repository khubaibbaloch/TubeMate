package com.TubeMateApp.tubemate.TubeMateApp.ui.HistoryScreen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenTopBar(
    title: String,
    onIconClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.shadow(1.dp),
        title = { Text(text = title, color = TubeMateThemes.colorScheme.textColor) },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        actions = {
            IconButton(onClick = { onIconClick() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Setting icon",
                    tint = TubeMateThemes.colorScheme.tint,
                )
            }
        },

    )
}
