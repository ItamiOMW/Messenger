package com.example.itami_chat.settings_feature.presentation.screens.settings

import com.example.itami_chat.core.domain.model.MyUser

data class SettingsState(
    val myUser: MyUser? = null,
)
