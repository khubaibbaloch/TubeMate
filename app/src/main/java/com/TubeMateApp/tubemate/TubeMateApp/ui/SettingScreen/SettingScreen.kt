package com.TubeMateApp.tubemate.TubeMateApp.ui.SettingScreen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.common.HomeScreenItemCard
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.common.HomeScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.SettingScreen.common.SettingScreenTopBar
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.BottomNavScreenRoutes

@Composable
fun SettingScreen(navController: NavController,viewModel: TubeMateViewModel) {
    val context = LocalContext.current

    // handling theme selection (light or dark)
    val isThemeClicked = remember { mutableStateOf(false) }
    val currentTheme = viewModel.currentTheme.collectAsState()
    val isLightThemeSelected = remember { mutableStateOf(currentTheme.value == "2") }

    Scaffold(
        topBar = {
            SettingScreenTopBar(
                title = "Settings",
                onIconClick = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState
                        == Lifecycle.State.RESUMED
                    ) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                /*Row(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {  isThemeClicked.value = true}
                ) {
                    Text(
                        text = "Theme",
                        fontSize = 16.sp,
                        color = Color.Black,
                    )
                }*/
                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .clickable { navController.navigate(BottomNavScreenRoutes.PrivacyPolicyScreen.route) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 16.sp,
                        color = Color.Black,
                    )
                }
                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .clickable {navController.navigate(BottomNavScreenRoutes.AboutUsScreen.route)}
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "About Us",
                        fontSize = 16.sp,
                        color = Color.Black,
                    )
                }
            }

            if (isThemeClicked.value) {
                AlertDialog(
                    containerColor = TubeMateThemes.colorScheme.onBackground,
                    onDismissRequest = {
                        isThemeClicked.value = false
                        // Update theme when dialog is dismissed
                        if (currentTheme.value == "2") isLightThemeSelected.value =
                            true else isLightThemeSelected.value = false

                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                isThemeClicked.value = false
                                if (isLightThemeSelected.value) {
                                    viewModel.updateTheme(context = context,newTheme ="2")
                                } else {
                                    viewModel.updateTheme(context = context, newTheme = "1")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color.LightGray)
                        ) {
                            Text(text = "Save", color = Color.Black)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                isThemeClicked.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Color.LightGray)
                        ) {
                            Text(text = "Cancel", color = Color.Black)
                        }
                    },
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Set the theme", style = TextStyle(
                                    fontSize = 16.sp,
                                    color = TubeMateThemes.colorScheme.textColor,
                                )
                            )
                        }
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10f))
                                    .clickable { isLightThemeSelected.value = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isLightThemeSelected.value,
                                    onClick = { isLightThemeSelected.value = true },
                                    colors = RadioButtonDefaults.colors(Color.LightGray)
                                )
                                Text(
                                    text = "Light Theme", style = TextStyle(
                                        fontSize = 14.sp, color = TubeMateThemes.colorScheme.textColor
                                    ), modifier = Modifier.padding(end = 16.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10f))
                                    .clickable { isLightThemeSelected.value = false },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = !isLightThemeSelected.value,
                                    onClick = { isLightThemeSelected.value = false },
                                    colors = RadioButtonDefaults.colors(Color.LightGray)
                                )
                                Text(
                                    text = "Dark Theme", style = TextStyle(
                                        fontSize = 14.sp, color = TubeMateThemes.colorScheme.textColor
                                    ), modifier = Modifier.padding(end = 16.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}