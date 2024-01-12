package com.oechslerbernardo.mongodbteste.presentation.login

import com.oechslerbernardo.mongodbteste.presentation.signup.SignUpEvent

sealed class LoginEvent {
    data class EmailChanged(val email: String): LoginEvent()
    data class PasswordChanged(val password: String): LoginEvent()
    object LoginClick : LoginEvent()
    object SignUpClick : LoginEvent()
    data class ShowSnackbar(val message: String): LoginEvent()
}