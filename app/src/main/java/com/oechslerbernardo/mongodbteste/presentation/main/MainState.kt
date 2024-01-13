package com.oechslerbernardo.mongodbteste.presentation.main

import com.oechslerbernardo.mongodbteste.data.Diary
import java.time.LocalDate

data class MainState(
    val isError: Boolean = false,
    val errorMessage: String? = null,
    var isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val diaries: Map<LocalDate, List<Diary>>? = null, // Change this line
    val isDrawerOpen: Boolean = false,
    var isDialogOpen: Boolean = false
)
