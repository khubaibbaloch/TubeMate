package com.WalkMateApp.walkmate.WalkMateApp.navGraph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.TubeMateApp.tubemate.TubeMateApp.MainViewModel.TubeMateViewModel
import com.TubeMateApp.tubemate.TubeMateApp.ui.HistoryScreen.HistoryScreen
import com.TubeMateApp.tubemate.TubeMateApp.ui.MainScreen.MainScreen

@Composable
fun RootNavGraph(navController: NavHostController,viewModel: TubeMateViewModel) {
    NavHost(navController = navController, ScreenRoutes.MainScreen.route){
        composable(ScreenRoutes.MainScreen.route) {
            MainScreen(viewModel=viewModel)
        }
    }
}
