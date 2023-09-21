package com.example.itami_chat.settings_feature.presentation.screens.privacy_setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.core.domain.preferences.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacySettingViewModel @Inject constructor(
    private val userManager: UserManager,
) : ViewModel() {

    var state by mutableStateOf(PrivacySettingState())
        private set

    init {
        getMyUser()
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                state = state.copy(myUser = myUser)
            }
        }
    }
}