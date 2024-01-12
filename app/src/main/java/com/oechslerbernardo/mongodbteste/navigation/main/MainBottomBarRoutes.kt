package com.oechslerbernardo.mongodbteste.navigation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainBottomBarRoutes(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : MainBottomBarRoutes(
        route = "home_screen",
        title = "Tasks",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )

    object CompletedTasks : MainBottomBarRoutes(
        route = "completed_tasks_screen",
        title = "Done",
        icon = Icons.Outlined.Check,
        selectedIcon = Icons.Filled.Check
    )
}