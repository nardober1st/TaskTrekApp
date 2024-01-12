package com.oechslerbernardo.mongodbteste.navigation.root

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oechslerbernardo.mongodbteste.navigation.auth.authNavGraph
import com.oechslerbernardo.mongodbteste.navigation.home.HomeRoutes
import com.oechslerbernardo.mongodbteste.navigation.home.homeNavGraph

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    isDarkTheme: Boolean
) {

    NavHost(
        navController = navController,
        route = Routes.RootGraphRoute.route,
        startDestination = startDestination
    ) {
        authNavGraph(navController = navController)
        homeNavGraph(navController, isDarkTheme)
    }
}