package com.oechslerbernardo.mongodbteste.presentation.write

import com.oechslerbernardo.mongodbteste.data.Diary
import io.realm.kotlin.types.RealmInstant

data class WriteState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val updatedDateTime: RealmInstant? = null,
    var isDialogOpen: Boolean = false
)