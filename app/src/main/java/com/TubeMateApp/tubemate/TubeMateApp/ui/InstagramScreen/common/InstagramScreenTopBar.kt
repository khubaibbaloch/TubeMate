package com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.ui.theme.Instagram
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstagramScreenTopBar(
    onIconClick: () -> Unit,
    title: String,
    onAppClick: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.shadow(0.dp),
        navigationIcon = {
            IconButton(onClick = { onIconClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Setting icon",
                    tint = TubeMateThemes.colorScheme.tint,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
        },
        title = { Text(text = title, color = TubeMateThemes.colorScheme.textColor) },
        colors = TopAppBarDefaults.topAppBarColors(Instagram),
        actions = {
            Image(
                painter = painterResource(id = R.drawable.social),
                contentDescription = "sdas",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(35.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onAppClick()  }

            )
        },

        )
}
