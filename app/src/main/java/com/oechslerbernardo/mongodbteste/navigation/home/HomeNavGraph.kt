package com.oechslerbernardo.mongodbteste.navigation.home

import android.util.Log
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
import com.oechslerbernardo.mongodbteste.navigation.main.MainBottomBarRoutes
import com.oechslerbernardo.mongodbteste.navigation.root.Routes
import com.oechslerbernardo.mongodbteste.presentation.main.MainEvent
import com.oechslerbernardo.mongodbteste.presentation.main.MainScreen
import com.oechslerbernardo.mongodbteste.presentation.main.MainViewModel
import com.oechslerbernardo.mongodbteste.presentation.write.WriteEvent
import com.oechslerbernardo.mongodbteste.presentation.write.WriteScreen
import com.oechslerbernardo.mongodbteste.presentation.write.WriteViewModel
import com.oechslerbernardo.mongodbteste.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Routes.HomeGraphRoute.route,
        startDestination = MainBottomBarRoutes.Home.route
    ) {
        composable(
            route = HomeRoutes.Write.route,
            arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            Log.d("NavigationLog", "Navigating to WriteScreen")
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