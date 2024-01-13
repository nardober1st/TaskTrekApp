package com.oechslerbernardo.mongodbteste.presentation.main

sealed class MainEvent {
    object OnSignOutClicked : MainEvent()
    object OnDeleteClicked : MainEvent()
    object OnDialogOpen : MainEvent()
    object OnDialogDismiss : MainEvent()
    data class OnDiaryClicked(val diaryId: String) : MainEvent()
    object OnAddDiaryClicked : MainEvent()
    data class ToggleThemeSwitch(val isDarkTheme: Boolean) : MainEvent()
}