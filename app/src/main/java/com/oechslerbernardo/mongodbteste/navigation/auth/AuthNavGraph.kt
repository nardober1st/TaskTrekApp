package com.oechslerbernardo.mongodbteste.navigation.auth

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.oechslerbernardo.mongodbteste.navigation.root.Routes
import com.oechslerbernardo.mongodbteste.presentation.login.LoginEvent
import com.oechslerbernardo.mongodbteste.presentation.login.LoginScreen
import com.oechslerbernardo.mongodbteste.presentation.login.LoginViewModel
import com.oechslerbernardo.mongodbteste.presentation.signup.SignUpEvent
import com.oechslerbernardo.mongodbteste.presentation.signup.SignUpScreen
import com.oechslerbernardo.mongodbteste.presentation.signup.SignupViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Routes.AuthGraphRoute.route,
        startDestination = AuthRoutes.LoginRoute.route
    ) {
        composable(route = AuthRoutes.LoginRoute.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val state = loginViewModel.state
            val scaffoldState = remember {
                SnackbarHostState()
            }
            LaunchedEffect(true) {
                loginViewModel.loginEvent.collect { event ->
                    when (event) {
                        is LoginEvent.LoginClick -> {
                            navController.popBackStack()
                            navController.navigate(Routes.HomeGraphRoute.route)
                        }

                        is LoginEvent.SignUpClick -> {
                            navController.navigate(AuthRoutes.SignupRoute.route)
                        }

                        is LoginEvent.ShowSnackbar -> {
                            scaffoldState.showSnackbar(
                                message = event.message,
                                duration = SnackbarDuration.Short
                            )
                        }

                        else -> {}
                    }
                }
            }
            LoginScreen(
                snackBar = scaffoldState,
                state = state,
                onEvent = loginViewModel::onEvent
            )
        }
        composable(AuthRoutes.SignupRoute.route) {
            val signupViewModel: SignupViewModel = hiltViewModel()
            val state = signupViewModel.state
            val scaffoldState = remember {
                SnackbarHostState()
            }
            LaunchedEffect(true) {
                signupViewModel.signupEvent.collect { event ->
                    when (event) {
                        is SignUpEvent.OnSignupClick -> {
                            navController.popBackStack()
                            navController.navigate(AuthRoutes.LoginRoute.route)
                        }

                        is SignUpEvent.ShowSnackbar -> {
                            scaffoldState.showSnackbar(
                                message = event.message,
                                duration = SnackbarDuration.Short
                            )
                        }

                        else -> {}
                    }
                }
            }
            SignUpScreen(
                snackBar = scaffoldState,
                state = state,
                onEvent = signupViewModel::onEvent
            )
        }
    }
}