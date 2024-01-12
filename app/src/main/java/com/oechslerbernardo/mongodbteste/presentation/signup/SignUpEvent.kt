package com.oechslerbernardo.mongodbteste.presentation.signup

sealed class SignUpEvent {
    data class EmailChanged(val email: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    data class NameChanged(val name: String) : SignUpEvent()
    object OnSignupClick : SignUpEvent()
    data class ShowSnackbar(val message: String): SignUpEvent()
}