package com.oechslerbernardo.mongodbteste.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.LayoutDirection
import com.oechslerbernardo.mongodbteste.domain.repository.Diaries
import com.oechslerbernardo.mongodbteste.presentation.components.DisplayAlertDialog
import com.oechslerbernardo.mongodbteste.presentation.home.EmptyPage
import com.oechslerbernardo.mongodbteste.presentation.home.HomeContent
import com.oechslerbernardo.mongodbteste.presentation.home.HomeEvent
import com.oechslerbernardo.mongodbteste.presentation.home.HomeState
import com.oechslerbernardo.mongodbteste.presentation.home.components.HomeTopBar
import com.oechslerbernardo.mongodbteste.presentation.home.components.NavigationDrawer
import com.oechslerbernardo.mongodbteste.util.RequestState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: HomeState,
    diaries: Diaries,
    drawerState: DrawerState,
    onEvent: (HomeEvent) -> Unit,
    isDarkTheme: Boolean
) {

    var padding by remember { mutableStateOf(PaddingValues()) }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    NavigationDrawer(drawerState = drawerState, onEvent = onEvent, isDarkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopBar(
                    scrollBehavior = scrollBehavior, onMenuClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onEvent = onEvent
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                    onClick = { onEvent(HomeEvent.OnAddDiaryClicked) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Create, contentDescription = "New Diary Icon"
                    )
                }
            },
            content = {
                padding = it
                when (diaries) {
                    is RequestState.Success -> {
                        HomeContent(
                            paddingValues = it,
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
        )
    }
}