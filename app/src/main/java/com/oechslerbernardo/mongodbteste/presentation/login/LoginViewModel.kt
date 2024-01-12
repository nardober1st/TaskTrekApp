package com.oechslerbernardo.mongodbteste.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oechslerbernardo.mongodbteste.data.repository.AuthRepositoryImpl
import com.oechslerbernardo.mongodbteste.presentation.signup.SignUpEvent
import com.oechslerbernardo.mongodbteste.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val _loginEvent = Channel<LoginEvent>()
    val loginEvent = _loginEvent.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is LoginEvent.ShowSnackbar -> {
                viewModelScope.launch {
                    _loginEvent.send(LoginEvent.ShowSnackbar(state.isError!!))
                }
            }

            is LoginEvent.LoginClick -> {
                viewModelScope.launch {
                    repository.login(state.email, state.password).collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                state = state.copy(
                                    isError = result.message,
                                    isLoading = false
                                )
                                _loginEvent.send(LoginEvent.ShowSnackbar(result.message!!))
                            }

                            is Resource.Loading -> {
                                state = state.copy(isLoading = true)
                            }

                            is Resource.Success -> {
                                state = state.copy(
                                    isSuccess = true,
                                    isLoading = false
                                )
                                _loginEvent.send(LoginEvent.LoginClick)
//                                _loginEvent.send(LoginEvent.ShowSnackbar(result.message!!))
                            }

                            else -> {}
                        }
                    }
                }
            }

            is LoginEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is LoginEvent.SignUpClick -> {
                viewModelScope.launch {
                    state = state.copy(isSignupClicked = true)
                    _loginEvent.send(LoginEvent.SignUpClick)
                }
            }

            else -> {}
        }
    }
}