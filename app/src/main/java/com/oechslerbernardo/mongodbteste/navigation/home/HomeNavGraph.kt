package com.oechslerbernardo.mongodbteste.navigation.home

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.oechslerbernardo.mongodbteste.navigation.root.Routes
import com.oechslerbernardo.mongodbteste.presentation.home.HomeEvent
import com.oechslerbernardo.mongodbteste.presentation.home.HomeScreen
import com.oechslerbernardo.mongodbteste.presentation.home.HomeViewModel
import com.oechslerbernardo.mongodbteste.presentation.write.WriteEvent
import com.oechslerbernardo.mongodbteste.presentation.write.WriteScreen
import com.oechslerbernardo.mongodbteste.presentation.write.WriteViewModel
import com.oechslerbernardo.mongodbteste.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean
) {
    navigation(
        route = Routes.HomeGraphRoute.route,
        startDestination = HomeRoutes.Home.route
    ) {
        composable(route = HomeRoutes.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val state = viewModel.state
            val diaries by viewModel.diaries
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            LaunchedEffect(key1 = true) {
                viewModel.homeChannelEvent.collect { event ->
                    when (event) {
                        is HomeEvent.OnSignOutClicked -> {
                            navController.popBackStack()
                            navController.navigate(Routes.AuthGraphRoute.route)
                        }

                        is HomeEvent.OnDiaryClicked -> {
                            navController.navigate(HomeRoutes.Write.passDiaryId(event.diaryId))
                        }

                        is HomeEvent.OnAddDiaryClicked -> {
                            navController.navigate(HomeRoutes.Write.route)
                        }

                        else -> {}
                    }
                }
            }

            HomeScreen(
                diaries = diaries,
                drawerState = drawerState,
                onEvent = viewModel::onEvent,
                state = state,
                isDarkTheme = isDarkTheme
            )
        }
        composable(
            route = HomeRoutes.Write.route,
            arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            val viewModel: WriteViewModel = viewModel()
            val state = viewModel.state

            LaunchedEffect(true) {
                viewModel.writeChannelEvent.collect { event ->
                    when (event) {
                        is WriteEvent.OnBackClick -> {
                            navController.popBackStack()
                        }

                        is WriteEvent.OnUpsertClick -> {
                            navController.popBackStack()
                        }

                        is WriteEvent.OnDeleteClick -> {
                            navController.popBackStack()
                        }

                        else -> {}
                    }
                }
            }

            WriteScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}