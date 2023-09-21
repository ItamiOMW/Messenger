package com.example.itami_chat.settings_feature.presentation.screens.settings

sealed class SettingsEvent {

    data object OnLogout: SettingsEvent()

}
