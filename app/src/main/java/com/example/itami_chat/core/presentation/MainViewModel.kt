package com.example.itami_chat.core.presentation

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
class MainViewModel @Inject constructor(
    private val appSettingsManager: AppSettingsManager
) : ViewModel() {


    var theme by mutableStateOf(Theme.Default)
        private set

    var isDarkMode by mutableStateOf(false)
        private set

    init {
        getCurrentTheme()
        getDarkModeState()
    }


    private fun getCurrentTheme(){
        viewModelScope.launch {
            appSettingsManager.currentTheme.collect { theme ->
                this@MainViewModel.theme = theme
            }
        }
    }

    private fun getDarkModeState(){
        viewModelScope.launch {
            appSettingsManager.isDarkMode.collect { isDarkMode ->
                this@MainViewModel.isDarkMode = isDarkMode
            }
        }
    }

}