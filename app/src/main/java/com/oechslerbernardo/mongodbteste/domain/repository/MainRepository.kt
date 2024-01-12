package com.oechslerbernardo.mongodbteste.domain.repository

import kotlinx.coroutines.flow.Flow

interface MainRepository {
    val darkThemeFlow: Flow<Boolean>
    suspend fun toggleTheme(isDark: Boolean)
}