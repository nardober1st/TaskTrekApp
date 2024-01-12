package com.oechslerbernardo.mongodbteste.presentation.home

sealed class HomeEvent {
    object OnSignOutClicked : HomeEvent()
    object OnDeleteClicked : HomeEvent()
    object OnDialogOpen : HomeEvent()
    object OnDialogDismiss : HomeEvent()
    data class OnDiaryClicked(val diaryId: String) : HomeEvent()
    object OnAddDiaryClicked : HomeEvent()
    data class ToggleThemeSwitch(val isDarkTheme: Boolean) : HomeEvent()
}