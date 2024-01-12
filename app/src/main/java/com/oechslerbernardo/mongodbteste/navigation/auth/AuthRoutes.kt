package com.oechslerbernardo.mongodbteste.navigation.auth

sealed class AuthRoutes(val route: String) {
    object LoginRoute : AuthRoutes(route = "login")
    object SignupRoute : AuthRoutes(route = "signup")
}
