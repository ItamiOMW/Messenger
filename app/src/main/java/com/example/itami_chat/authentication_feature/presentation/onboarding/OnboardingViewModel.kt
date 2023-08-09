package com.example.itami_chat.authentication_feature.presentation.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.core.domain.preferences.AppSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appSettingsManager: AppSettingsManager,
) : ViewModel() {


    var state by mutableStateOf(OnboardingState())
        private set

    init {
        getDarkModeState()
    }

    private var changeDarkModeJob: Job? = null


    fun onEvent(onboardingEvent: OnboardingEvent) {
        when (onboardingEvent) {
            OnboardingEvent.ChangeDarkModeState -> {
                changeDarkModeState()
            }
        }
    }

    private fun changeDarkModeState() {
        changeDarkModeJob?.cancel()
        changeDarkModeJob = viewModelScope.launch {
            appSettingsManager.changeDarkModeState(!state.isDarkTheme)
        }
    }

    private fun getDarkModeState() {
        viewModelScope.launch {
            appSettingsManager.isDarkMode.collect { darkModeState ->
                state = state.copy(isDarkTheme = darkModeState)
            }
        }
    }

}