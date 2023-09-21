package com.example.itami_chat.settings_feature.presentation.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.domain.use_case.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userManager: UserManager,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiEvent = Channel<SettingsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SettingsState())
        private set

    init {
        getMyUser()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnLogout -> {
                logout()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiEvent.send(SettingsUiEvent.OnLogout)
        }
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                state = state.copy(myUser = myUser)
            }
        }
    }

}