package com.oechslerbernardo.mongodbteste.presentation.write

import com.oechslerbernardo.mongodbteste.data.Diary
import java.time.ZonedDateTime

sealed class WriteEvent {
    data class TitleChanged(val title: String) : WriteEvent()
    data class DescriptionChanged(val description: String) : WriteEvent()
    object OnSaveClick : WriteEvent()
    object OnDeleteClick : WriteEvent()
    object OnBackClick: WriteEvent()
    object OnDialogDismiss: WriteEvent()
    object OnDialogOpen: WriteEvent()
    data class OnUpsertClick(val diary: Diary?): WriteEvent()
    data class OnUpdateDateTime(val zonedDateTime: ZonedDateTime): WriteEvent()
}
