package com.TubeMateApp.tubemate.TubeMateApp.ui.AboutUsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.TubeMateApp.ui.AboutUsScreen.common.AboutUsScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramScreenTopBar
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes

@Composable
fun AboutUsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AboutUsScreenTopBar(
                title = "About Us",
                onIconClick = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState
                        == Lifecycle.State.RESUMED
                    ) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TubeMate",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = TubeMateThemes.colorScheme.textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "(Media Downloader)",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = TubeMateThemes.colorScheme.textColor
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Version 1.0.0",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = TubeMateThemes.colorScheme.textColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TubeMateThemes.colorScheme.textColor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_svg), // Update with your actual logo resource
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Created by",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = TubeMateThemes.colorScheme.textColor
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Parvez Mayar & Khubaib Aziz",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = TubeMateThemes.colorScheme.textColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Owned by KP Creative Labs",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = TubeMateThemes.colorScheme.textColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TubeMate was crafted by Parvez Mayar and Khubaib Aziz, two tech enthusiasts from the heart of Lasbela (Balochistan), where the winds of innovation blow faintly. Despite the limited tech landscape, their passion for creating seamless experiences led them to embark on this journey. With TubeMate, they bring you a tool to enhance your media experience, making downloading media from various platforms easy and efficient. Join us on this journey where every download counts and every piece of media tells a story.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify,
                color = TubeMateThemes.colorScheme.textColor
            )
        }
    }
}
