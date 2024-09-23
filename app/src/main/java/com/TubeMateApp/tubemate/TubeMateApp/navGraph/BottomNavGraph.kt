package com.TubeMateApp.tubemate.TubeMateApp.navGraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.AboutUsScreen.AboutUsScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.FacebookScreen.FacebookScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.HistoryScreen.HistoryScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.HomeScreen.HomeScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.InstagramScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.PrivacyPolicyScreen.PrivacyPolicyScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.SettingScreen.SettingScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.WhatsappScreen.WhatsAppScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.YouTubeScreen
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.BottomNavScreenRoutes
import com.powervpn.PowerVPNApp.PowerVPN.ui.settings.AppUpdate.AppUpdateScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BottomNavGraph(navController: NavHostController,viewModel: TubeMateViewModel){
    NavHost(navController = navController, startDestination = BottomNavScreenRoutes.HomeScreen.route,
        enterTransition = {
            fadeIn(animationSpec = tween(200))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {  fadeIn(
            animationSpec = tween(200)
        )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200))
        }){
        composable(BottomNavScreenRoutes.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(BottomNavScreenRoutes.HistoryScreen.route) {
            HistoryScreen(navController,viewModel)
        }
        composable(BottomNavScreenRoutes.SettingScreen.route) {
            SettingScreen(navController,viewModel)
        }
        composable(BottomNavScreenRoutes.InstagramScreen.route) {
            InstagramScreen(navController,viewModel)
        }
        composable(BottomNavScreenRoutes.WhatsAppScreen.route) {
            WhatsAppScreen(navController,viewModel)
        }
        composable(BottomNavScreenRoutes.FacebookScreen.route) {
            FacebookScreen(navController,viewModel)
        }
        composable(BottomNavScreenRoutes.YouTubeScreen.route) {
            YouTubeScreen(navController,viewModel)
        }
        composable(BottomNavScreenRoutes.PrivacyPolicyScreen.route) {
            PrivacyPolicyScreen(navController)
        }
        composable(BottomNavScreenRoutes.AboutUsScreen.route) {
            AboutUsScreen(navController)
        }
        composable(BottomNavScreenRoutes.AppUpdateScreen.route) {
            AppUpdateScreen(navController)
        }
    }
}