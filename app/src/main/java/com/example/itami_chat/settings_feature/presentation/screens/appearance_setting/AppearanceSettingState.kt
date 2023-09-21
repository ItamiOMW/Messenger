package com.example.itami_chat.settings_feature.presentation.screens.appearance_setting

import com.example.itami_chat.core.domain.model.Theme

data class AppearanceSettingState(
    val theme: Theme = Theme.Default,
    val isDarkMode: Boolean = false,
)
