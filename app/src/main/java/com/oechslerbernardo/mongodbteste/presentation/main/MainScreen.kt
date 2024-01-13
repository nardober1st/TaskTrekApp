package com.oechslerbernardo.mongodbteste.presentation.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oechslerbernardo.mongodbteste.domain.repository.Diaries
import com.oechslerbernardo.mongodbteste.navigation.main.MainBottomBarRoutes
import com.oechslerbernardo.mongodbteste.navigation.main.MainNavGraph
import com.oechslerbernardo.mongodbteste.presentation.components.DisplayAlertDialog
import com.oechslerbernardo.mongodbteste.presentation.main.components.HomeTopBar
import com.oechslerbernardo.mongodbteste.presentation.main.components.NavigationDrawer
import com.oechslerbernardo.mongodbteste.util.RequestState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    state: MainState,
    diaries: Diaries,
    drawerState: DrawerState,
    onEvent: (MainEvent) -> Unit,
    isDarkTheme: Boolean,
    navController: NavHostController = rememberNavController(),
) {

    Log.d("TAGY", "Composing HomeScreen with diaries state: $diaries")

    var padding by remember { mutableStateOf(PaddingValues()) }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    NavigationDrawer(drawerState = drawerState, onEvent = onEvent, isDarkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopBar(
                    scrollBehavior = scrollBehavior, onMenuClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onEvent = onEvent
                )
            },
            bottomBar = {
                BottomBar(navController = navController)
            },
            content = {
                MainNavGraph(
                    navController = navController,
                    diaries = diaries,
                    onEvent = onEvent
                )

                DisplayAlertDialog(
                    title = "Sign Out",
                    message = "Are you sure you want to sign out?",
                    dialogOpened = state.isDialogOpen,
                    onDialogClosed = { onEvent(MainEvent.OnDialogDismiss) },
                    onYesClicked = { onEvent(MainEvent.OnSignOutClicked) }
                )

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        MainBottomBarRoutes.Home,
        MainBottomBarRoutes.CompletedTasks
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    /*
        THIS WAY WE CAN HIDE THE BOTTOM BAR,
        IF OUR CURRENT DESTINATION IS NOT ONE
        OF THE BOTTOM BAR DESTINATION
        EX: HOME, MENU, SETTINGS
    */

//    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
//    if (bottomBarDestination) {
//        NavigationBar(
//            containerColor = Blue02
//        ) {
//            screens.forEach { screen ->
//                AddItem(
//                    screen = screen,
//                    currentDestination = currentDestination,
//                    navController = navController
//                )
//            }
//        }
//    }

    NavigationBar() {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: MainBottomBarRoutes,
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val icon = if (isSelected) {
        screen.selectedIcon
    } else {
        screen.icon
    }

    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = isSelected,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}