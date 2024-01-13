package com.oechslerbernardo.mongodbteste.presentation.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oechslerbernardo.mongodbteste.data.repository.AuthRepositoryImpl
import com.oechslerbernardo.mongodbteste.data.repository.MainRepositoryImpl
import com.oechslerbernardo.mongodbteste.data.repository.MongoRepositoryImpl
import com.oechslerbernardo.mongodbteste.domain.repository.Diaries
import com.oechslerbernardo.mongodbteste.util.RequestState
import com.oechslerbernardo.mongodbteste.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
    private val mainRepositoryImpl: MainRepositoryImpl
) : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    // Theme related properties
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    var state by mutableStateOf(MainState())
        private set

    private var _mainChannelEvent = Channel<MainEvent>()
    var mainChannelEvent = _mainChannelEvent.receiveAsFlow()

    init {
        observeAllDiaries()
        viewModelScope.launch {
            mainRepositoryImpl.darkThemeFlow.collect { isDarkTheme ->
                _isDarkTheme.value = isDarkTheme
                Log.d("ThemeSwitch", "Initial theme state: $isDarkTheme")
            }
        }
    }

    fun onEvent(event: MainEvent) {
        Log.d("TAGY", "Handling event: $event")
        when (event) {
            is MainEvent.OnSignOutClicked -> {
                viewModelScope.launch {
                    _mainChannelEvent.send(MainEvent.OnSignOutClicked)
                    logout()
                }
            }

            is MainEvent.OnDialogOpen -> {
                state = state.copy(isDialogOpen = true)
            }

            is MainEvent.OnDialogDismiss -> {
                state = state.copy(isDialogOpen = false)
            }

            is MainEvent.OnDiaryClicked -> {
                viewModelScope.launch {
                    _mainChannelEvent.send(MainEvent.OnDiaryClicked(event.diaryId))
                }
            }

            is MainEvent.OnAddDiaryClicked -> {
                viewModelScope.launch {
                    _mainChannelEvent.send(MainEvent.OnAddDiaryClicked)
                }
            }

            is MainEvent.ToggleThemeSwitch -> {
                Log.d("ThemeSwitch", "Received ToggleThemeSwitch event")
                toggleTheme(event.isDarkTheme)
            }

            else -> {}
        }
    }

    private fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            mainRepositoryImpl.toggleTheme(isDark)
            Log.d("ThemeSwitch", "Updated isDarkTheme state to: ${_isDarkTheme.value}")
        }
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoRepositoryImpl.getAllDiaries().collect { result ->
                Log.d("TAGY", "Diaries collected: $result")
                diaries.value = result
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout().collect { result ->
                state = when (result) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        state.copy(isLoading = false, isSuccess = true, isDialogOpen = false)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, isError = true)
                    }
                }
            }
        }
    }
}