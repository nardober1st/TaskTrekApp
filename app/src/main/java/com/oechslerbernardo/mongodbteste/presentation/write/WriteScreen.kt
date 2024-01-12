package com.oechslerbernardo.mongodbteste.presentation.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.oechslerbernardo.mongodbteste.data.Diary
import com.oechslerbernardo.mongodbteste.presentation.write.components.WriteTopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    state: WriteState,
    onEvent: (WriteEvent) -> Unit
) {

    Scaffold(
        topBar = {
            WriteTopBar(state = state, onEvent = onEvent)
        },
        content = { paddingValues ->
            WriteContent(
                paddingValues = paddingValues,
                state = state,
                onEvent = onEvent
            )
        }
    )
}