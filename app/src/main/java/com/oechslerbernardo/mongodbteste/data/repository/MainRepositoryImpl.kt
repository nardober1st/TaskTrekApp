package com.oechslerbernardo.mongodbteste.data.repository

import com.oechslerbernardo.mongodbteste.data.PreferencesManager
import com.oechslerbernardo.mongodbteste.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : MainRepository {

    override val darkThemeFlow: Flow<Boolean> = preferencesManager.darkThemeFlow

    override suspend fun toggleTheme(isDark: Boolean) {
        preferencesManager.toggleTheme(isDark)
    }
}