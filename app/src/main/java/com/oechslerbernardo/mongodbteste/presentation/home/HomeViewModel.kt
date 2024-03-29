package com.oechslerbernardo.mongodbteste.presentation.home

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
import com.oechslerbernardo.mongodbteste.navigation.home.HomeRoutes
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
class HomeViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
    private val mainRepositoryImpl: MainRepositoryImpl
) : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    // Theme related properties
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    var state by mutableStateOf(HomeState())
        private set

    private var _homeChannelEvent = Channel<HomeEvent>()
    var homeChannelEvent = _homeChannelEvent.receiveAsFlow()

    init {
        observeAllDiaries()
        viewModelScope.launch {
            mainRepositoryImpl.darkThemeFlow.collect { isDarkTheme ->
                _isDarkTheme.value = isDarkTheme
                Log.d("ThemeSwitch", "Initial theme state: $isDarkTheme")
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        Log.d("TAGY", "Handling event: $event")
        when (event) {
            is HomeEvent.OnSignOutClicked -> {
                viewModelScope.launch {
                    _homeChannelEvent.send(HomeEvent.OnSignOutClicked)
                    logout()
                }
            }

            is HomeEvent.OnDialogOpen -> {
                state = state.copy(isDialogOpen = true)
            }

            is HomeEvent.OnDialogDismiss -> {
                state = state.copy(isDialogOpen = false)
            }

            is HomeEvent.OnDiaryClicked -> {
                viewModelScope.launch {
                    _homeChannelEvent.send(HomeEvent.OnDiaryClicked(event.diaryId))
                }
            }

            is HomeEvent.OnAddDiaryClicked -> {
                viewModelScope.launch {
                    _homeChannelEvent.send(HomeEvent.OnAddDiaryClicked)
                }
            }

            is HomeEvent.ToggleThemeSwitch -> {
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