package com.WalkMateApp.walkmate.WalkMateApp.navGraph

import com.TubeMateApp.tubemate.R

sealed class BottomNavScreenRoutes(val route: String) {
    data object HomeScreen : BottomNavScreenRoutes("HomeScreenRoute")
    data object HistoryScreen : BottomNavScreenRoutes("HistoryScreenRoute")
    data object SettingScreen : BottomNavScreenRoutes("SettingScreenRoute")
    data object InstagramScreen : BottomNavScreenRoutes("InstagramScreenRoute")
    data object WhatsAppScreen : BottomNavScreenRoutes("WhatsAppScreenRoute")
    data object FacebookScreen : BottomNavScreenRoutes("FacebookScreenRoute")
    data object YouTubeScreen : BottomNavScreenRoutes("YouTubeScreenRoute")
    data object PrivacyPolicyScreen : BottomNavScreenRoutes("PrivacyPolicyScreenRoute")
    data object AboutUsScreen : BottomNavScreenRoutes("AboutUsScreenRoute")
    data object AppUpdateScreen : BottomNavScreenRoutes("AppUpdateScreen")
}

sealed class ScreenRoutes(val route: String) {
    data object MainScreen : ScreenRoutes("MainScreenRoute")
}
