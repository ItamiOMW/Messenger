package com.example.itami_chat.settings_feature.presentation.screens.appearance_setting

import com.example.itami_chat.core.domain.model.Theme

sealed class AppearanceSettingEvent {

    data class OnChangeTheme(val theme: Theme): AppearanceSettingEvent()

    data object OnChangeDarkModeState: AppearanceSettingEvent()

}