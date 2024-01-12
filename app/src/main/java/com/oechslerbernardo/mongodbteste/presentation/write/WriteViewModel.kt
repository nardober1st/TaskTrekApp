package com.oechslerbernardo.mongodbteste.presentation.write

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oechslerbernardo.mongodbteste.data.Diary
import com.oechslerbernardo.mongodbteste.data.repository.MongoRepositoryImpl
import com.oechslerbernardo.mongodbteste.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.oechslerbernardo.mongodbteste.util.RequestState
import com.oechslerbernardo.mongodbteste.util.toRealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(WriteState())
        private set

    private var _writeChannelEvent = Channel<WriteEvent>()
    var writeChannelEvent = _writeChannelEvent.receiveAsFlow()

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    fun onEvent(event: WriteEvent) {
        when (event) {
            is WriteEvent.OnDialogDismiss -> {
                state = state.copy(isDialogOpen = false)
            }

            is WriteEvent.OnDialogOpen -> {
                state = state.copy(isDialogOpen = true)
            }

            is WriteEvent.TitleChanged -> {
                state = state.copy(
                    title = event.title
                )
            }

            is WriteEvent.OnBackClick -> {
                viewModelScope.launch {
                    _writeChannelEvent.send(WriteEvent.OnBackClick)
                }
            }

            is WriteEvent.DescriptionChanged -> {
                state = state.copy(
                    description = event.description
                )
            }

            is WriteEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    // Construct the Diary object from the current state
                    _writeChannelEvent.send(WriteEvent.OnDeleteClick)
                    deleteDiary()
                }
            }

            is WriteEvent.OnUpsertClick -> {
                viewModelScope.launch {
                    _writeChannelEvent.send(WriteEvent.OnUpsertClick(event.diary))
                    event.diary?.let { upsertDiary(it) }
                }
            }

            is WriteEvent.OnUpdateDateTime -> {
                state = state.copy(
                    updatedDateTime = event.zonedDateTime.toInstant().toRealmInstant()
                )
            }

            else -> {}
        }
    }

    private fun getDiaryIdArgument() {
        val diaryId = savedStateHandle.get<String>(key = WRITE_SCREEN_ARGUMENT_KEY)
        Log.d("WriteViewModel", "Received diaryId: $diaryId")
        state = state.copy(
            selectedDiaryId = diaryId
        )
    }

    private fun fetchSelectedDiary() {
        if (state.selectedDiaryId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                MongoRepositoryImpl.getSelectedDiary(diaryId = ObjectId.invoke(state.selectedDiaryId!!))
                    .collect { diary ->
                        if (diary is RequestState.Success) {
                            state = state.copy(description = diary.data.description)
                            state = state.copy(title = diary.data.title)
                            state = state.copy(selectedDiary = diary.data)
                        }
                    }
            }
        }
    }

    private fun upsertDiary(
        diary: Diary
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.selectedDiaryId != null) {
                updateDiary(diary)
            } else {
                insertDiary(diary)
            }
        }
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime) {
        state = state.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
    }

    private suspend fun insertDiary(
        diary: Diary,
    ) {
        val result = MongoRepositoryImpl.insertDiary(diary = diary.apply {
            if (state.updatedDateTime != null) {
                date = state.updatedDateTime!!
            }
        })

        when (result) {
            is RequestState.Loading -> {
                state = state.copy(isLoading = true)
            }

            is RequestState.Error -> {
                state = state.copy(isLoading = false)
            }

            is RequestState.Success -> {
                state = state.copy(isLoading = false)
            }

            else -> {}
        }
    }

    private suspend fun updateDiary(
        diary: Diary
    ) {
        val result = MongoRepositoryImpl.updateDiary(diary = diary.apply {
            _id = io.realm.kotlin.types.ObjectId.from(state.selectedDiaryId!!)
            date = if (state.updatedDateTime != null) {
                state.updatedDateTime!!
            } else {
                state.selectedDiary!!.date
            }
        })
        when (result) {
            is RequestState.Loading -> {
                state = state.copy(isLoading = true)
            }

            is RequestState.Error -> {
                state = state.copy(isLoading = false)
            }

            is RequestState.Success -> {
                state = state.copy(isLoading = false)
            }

            else -> {}
        }
    }

    private fun deleteDiary() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.selectedDiaryId != null) {
                val result = MongoRepositoryImpl.deleteDiary(
                    id = io.realm.kotlin.types.ObjectId.Companion.from(state.selectedDiaryId!!)
                )
                when (result) {
                    is RequestState.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is RequestState.Success -> {
                        state = state.copy(isLoading = false, isDialogOpen = false)
                    }

                    is RequestState.Error -> {
                        state = state.copy(isLoading = false)
                    }

                    else -> {}
                }
            }
        }
    }
}