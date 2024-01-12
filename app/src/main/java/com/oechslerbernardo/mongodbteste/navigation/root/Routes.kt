package com.oechslerbernardo.mongodbteste.navigation.root

sealed class Routes(val route: String) {
    object RootGraphRoute : Routes(route = "root")
    object AuthGraphRoute : Routes(route = "auth")
    object MainGraphRoute : Routes(route = "main")
    object HomeGraphRoute : Routes(route = "home")
}
