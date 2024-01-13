package com.oechslerbernardo.mongodbteste.navigation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oechslerbernardo.mongodbteste.domain.repository.Diaries
import com.oechslerbernardo.mongodbteste.navigation.home.HomeRoutes
import com.oechslerbernardo.mongodbteste.navigation.home.homeNavGraph
import com.oechslerbernardo.mongodbteste.navigation.root.Routes
import com.oechslerbernardo.mongodbteste.presentation.completedTasks.CompletedTasksScreen
import com.oechslerbernardo.mongodbteste.presentation.home.EmptyPage
import com.oechslerbernardo.mongodbteste.presentation.home.HomeContent
import com.oechslerbernardo.mongodbteste.presentation.home.HomeScreen
import com.oechslerbernardo.mongodbteste.presentation.main.MainEvent
import com.oechslerbernardo.mongodbteste.presentation.main.MainViewModel
import com.oechslerbernardo.mongodbteste.presentation.write.WriteScreen
import com.oechslerbernardo.mongodbteste.presentation.write.WriteViewModel
import com.oechslerbernardo.mongodbteste.util.RequestState


@Composable
fun MainNavGraph(
    navController: NavHostController,
    diaries: Diaries,
    onEvent: (MainEvent) -> Unit
) {
    NavHost(navController, startDestination = MainBottomBarRoutes.Home.route) {
        composable(MainBottomBarRoutes.Home.route) {
            HomeScreen(diaries = diaries, onEvent = onEvent)
        }
        composable(MainBottomBarRoutes.CompletedTasks.route) {
            CompletedTasksScreen()
        }
        homeNavGraph(navController)
    }
}