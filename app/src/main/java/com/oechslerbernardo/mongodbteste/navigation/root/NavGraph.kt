package com.oechslerbernardo.mongodbteste.navigation.root

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oechslerbernardo.mongodbteste.navigation.auth.authNavGraph
import com.oechslerbernardo.mongodbteste.navigation.home.HomeRoutes
import com.oechslerbernardo.mongodbteste.navigation.home.homeNavGraph
import com.oechslerbernardo.mongodbteste.presentation.main.MainEvent
import com.oechslerbernardo.mongodbteste.presentation.main.MainScreen
import com.oechslerbernardo.mongodbteste.presentation.main.MainViewModel

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
        composable(route = Routes.MainGraphRoute.route) {
            val viewModel: MainViewModel = hiltViewModel()
            val state = viewModel.state
            val diaries by viewModel.diaries
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            LaunchedEffect(key1 = true) {
                viewModel.mainChannelEvent.collect { event ->
                    Log.d("NavigationLog", "Event received: $event")
                    when (event) {
                        is MainEvent.OnSignOutClicked -> {
                            navController.popBackStack()
                            navController.navigate(Routes.AuthGraphRoute.route)
                        }

                        is MainEvent.OnDiaryClicked -> {
                            Log.d("NavigationLog", "Navigating to Write screen with diaryId: ${event.diaryId}")
                            navController.navigate(HomeRoutes.Write.passDiaryId(event.diaryId))
                        }

                        else -> {}
                    }
                }
            }

            MainScreen(
                diaries = diaries,
                drawerState = drawerState,
                onEvent = viewModel::onEvent,
                state = state,
                isDarkTheme = isDarkTheme
            )
        }
    }
}