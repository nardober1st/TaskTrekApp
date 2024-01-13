package com.oechslerbernardo.mongodbteste.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.oechslerbernardo.mongodbteste.domain.repository.Diaries
import com.oechslerbernardo.mongodbteste.presentation.main.MainEvent
import com.oechslerbernardo.mongodbteste.presentation.main.MainState
import com.oechslerbernardo.mongodbteste.util.RequestState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    diaries: Diaries,
    onEvent: (MainEvent) -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                onClick = { onEvent(MainEvent.OnAddDiaryClicked) }
            ) {
                Icon(
                    imageVector = Icons.Default.Create, contentDescription = "New Diary Icon"
                )
            }
        },
    ) { paddingValues ->
//            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

        padding = paddingValues
        when (diaries) {
            is RequestState.Success -> {
                HomeContent(
                    paddingValues = padding,
                    diaryNotes = diaries.data,
                    onEvent = onEvent
                )
            }

            is RequestState.Error -> {
                EmptyPage(
                    title = "Error", subtitle = "${diaries.error}"
                )
            }

            is RequestState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {}
        }
    }
}