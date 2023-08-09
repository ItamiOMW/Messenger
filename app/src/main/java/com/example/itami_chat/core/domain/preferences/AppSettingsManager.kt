package com.example.itami_chat.core.domain.preferences

import com.example.itami_chat.core.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface AppSettingsManager {

    val currentTheme: Flow<Theme>

    suspend fun changeTheme(theme: Theme)

    val isDarkMode: Flow<Boolean>

    suspend fun changeDarkModeState(enabled: Boolean)

}