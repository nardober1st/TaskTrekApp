package com.oechslerbernardo.mongodbteste.navigation.home

import com.oechslerbernardo.mongodbteste.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

sealed class HomeRoutes(val route: String) {
    object Home : HomeRoutes(route = "home_screen")
    object Write : HomeRoutes(route = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}") {
        fun passDiaryId(diaryId: String) =
            "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=$diaryId"
    }
}
