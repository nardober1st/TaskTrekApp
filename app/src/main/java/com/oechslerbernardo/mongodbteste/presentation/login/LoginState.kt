package com.oechslerbernardo.mongodbteste.presentation.login

data class LoginState(
    var email: String = "",
    var password: String = "",
    var emailRecover: String = "",
    var isLoginClicked: Boolean = false,
    var isSignupClicked: Boolean = false,
    var isLoading: Boolean = false,
    var isError: String? = "",
    val isSuccess: Boolean = false
)