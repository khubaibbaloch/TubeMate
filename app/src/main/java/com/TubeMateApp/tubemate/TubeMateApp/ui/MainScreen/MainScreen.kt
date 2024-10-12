package com.TubeMateApp.tubemate.TubeMateApp.ui.MainScreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.navGraph.BottomNavGraph
import com.TubeMateApp.tubemate.TubeMateApp.ui.HistoryScreen.HistoryScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.HomeScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.MainScreen.common.CustomBottomNav
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes
import com.TubeMateApp.tubemate.ui.theme.navigationColor
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.BottomNavScreenRoutes
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.ScreenRoutes
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.powervpn.PowerVPNApp.PowerVPN.inAppUpdate.CheckForUpdates

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TubeMateViewModel,
    notificationData: String
) {


    val navController = rememberNavController()


    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = true
    )

    systemUiController.setNavigationBarColor(
        color = Color.Transparent,
        darkIcons = true
    )

    LaunchedEffect(notificationData) {
        if (notificationData == "update") {
            navController.navigate(BottomNavScreenRoutes.AppUpdateScreen.route)
        }
    }

    CheckForUpdates()

    Scaffold(
        modifier = Modifier
            .background(Color.Gray)
            .navigationBarsPadding(),
        bottomBar = {
            if (shouldShowBottomBar(navController)) {
                CustomBottomNav(navController = navController)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            BottomNavGraph(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun shouldShowBottomBar(navController: NavController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute in listOf(
        BottomNavScreenRoutes.HomeScreen.route,
        BottomNavScreenRoutes.HistoryScreen.route
    )
}
