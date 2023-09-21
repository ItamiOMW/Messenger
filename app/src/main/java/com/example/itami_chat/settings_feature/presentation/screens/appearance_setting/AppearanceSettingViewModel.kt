package com.example.itami_chat.settings_feature.presentation.screens.appearance_setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.domain.preferences.AppSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppearanceSettingViewModel @Inject constructor(
    private val appSettingsManager: AppSettingsManager,
) : ViewModel() {

    var state by mutableStateOf(AppearanceSettingState())
        private set

    init {
        getDarkModeState()
        getCurrentTheme()
    }

    fun onEvent(event: AppearanceSettingEvent) {
        when (event) {
            is AppearanceSettingEvent.OnChangeDarkModeState -> {
                changeDarkModeState(!state.isDarkMode)
            }

            is AppearanceSettingEvent.OnChangeTheme -> {
                if (state.theme != event.theme) {
                    changeTheme(event.theme)
                }
            }
        }
    }

    private fun getDarkModeState() {
        viewModelScope.launch {
            appSettingsManager.isDarkMode.collect { isDarkMode ->
                state = state.copy(isDarkMode = isDarkMode)
            }
        }
    }

    private fun getCurrentTheme() {
        viewModelScope.launch {
            appSettingsManager.currentTheme.collect { theme ->
                state = state.copy(theme = theme)
            }
        }
    }

    private fun changeTheme(theme: Theme) {
        viewModelScope.launch {
            appSettingsManager.changeTheme(theme)
        }
    }

    private fun changeDarkModeState(isDarkMode: Boolean) {
        viewModelScope.launch {
            appSettingsManager.changeDarkModeState(isDarkMode)
        }
    }

}