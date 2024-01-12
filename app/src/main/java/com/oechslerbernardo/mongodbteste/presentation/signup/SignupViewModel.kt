package com.oechslerbernardo.mongodbteste.presentation.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oechslerbernardo.mongodbteste.data.repository.AuthRepositoryImpl
import com.oechslerbernardo.mongodbteste.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// In your ViewModel or a similar lifecycle-aware component
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(SignupState())
        private set

    private val _signupEvent = Channel<SignUpEvent>()
    val signupEvent = _signupEvent.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is SignUpEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is SignUpEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is SignUpEvent.ShowSnackbar -> {
                viewModelScope.launch {
                    _signupEvent.send(SignUpEvent.ShowSnackbar(state.isError!!))
                }
            }

            is SignUpEvent.OnSignupClick -> {
                viewModelScope.launch {
                    repository.signup(state.email, state.password).collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                state = state.copy(isLoading = true)
                            }

                            is Resource.Error -> {
                                state = state.copy(
                                    isError = result.message,
                                    isLoading = false
                                )
                                _signupEvent.send(SignUpEvent.ShowSnackbar(result.message!!))
                            }

                            is Resource.Success -> {
                                state = state.copy(
                                    isSuccess = true,
                                    isLoading = false
                                )
                                _signupEvent.send(SignUpEvent.OnSignupClick)
                            }

                            else -> {}
                        }
                    }
                }
            }

            else -> {}
        }
    }
}