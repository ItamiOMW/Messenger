package com.example.itami_chat.settings_feature.presentation.screens.verify_password_change

import com.example.itami_chat.core.domain.model.MyUser

data class VerifyPasswordChangeState(
    val myUser: MyUser? = null,

    val isLoading: Boolean = false,
)
