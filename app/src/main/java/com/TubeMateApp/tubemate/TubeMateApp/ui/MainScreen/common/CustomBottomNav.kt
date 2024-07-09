package com.TubeMateApp.tubemate.TubeMateApp.ui.MainScreen.common

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.TubeMateApp.tubemate.R
import com.TubeMateApp.tubemate.ui.theme.TubeMateThemes
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.BottomNavScreenRoutes
import com.WalkMateApp.walkmate.WalkMateApp.navGraph.ScreenRoutes


@Composable
fun CustomBottomNav(
    navController: NavController,

    ) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .shadow(1.dp)
            .fillMaxWidth()
            .height(52.dp)
         ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {


        Column(modifier = Modifier
            .padding(top = 3.dp)
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = {
                if (currentDestination != BottomNavScreenRoutes.HomeScreen.route ) {
                    navController.navigate(BottomNavScreenRoutes.HomeScreen.route)
                }
            }),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            /*Image(
                painter = painterResource(id = if (currentDestination == BottomNavScreenRoutes.HomeScreen.route) R.drawable.home_fill_icon else R.drawable.home_outline_icon),
                contentDescription = "adsasd",
                modifier = Modifier.size(24.dp)
            )*/Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "songs home button",
                modifier = Modifier.size(24.dp),
                tint = if (currentDestination == BottomNavScreenRoutes.HomeScreen.route) Color.Black.copy(.5f) else Color.Black,
            )
        }


        Column(modifier = Modifier
            .padding(top = 3.dp)
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = {
                if (currentDestination != BottomNavScreenRoutes.HistoryScreen.route ) {
                    navController.navigate(BottomNavScreenRoutes.HistoryScreen.route)
                }
            }),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            /*Image(
                painter = painterResource(id = if (currentDestination == BottomNavScreenRoutes.HistoryScreen.route) R.drawable.history_fill_icon else R.drawable.history_outline_icon),
                contentDescription = "adsasd",
                modifier = Modifier.size(24.dp)
            )*/
            Icon(
                painter = painterResource(id = R.drawable.history),
                contentDescription = "songs home button",
                modifier = Modifier.size(24.dp),
                tint = if (currentDestination == BottomNavScreenRoutes.HistoryScreen.route) Color.Black.copy(.5f) else Color.Black,
            )
        }
    }
}