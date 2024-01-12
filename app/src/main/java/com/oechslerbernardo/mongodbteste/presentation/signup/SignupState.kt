package com.oechslerbernardo.mongodbteste.presentation.signup

data class SignupState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    var isCadastroClicked: Boolean = false,
    var isLoading: Boolean = false,
    var isError: String? = "",
    var isSuccess: Boolean = false
)