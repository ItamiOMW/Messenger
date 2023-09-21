package com.example.itami_chat.settings_feature.presentation.screens.messages_permission

import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.domain.model.MyUser

data class MessagesPermissionState(
    val myUser: MyUser? = null,
    val selectedPermission: MessagesPermission = MessagesPermission.ANYONE,
    val isLoading: Boolean = false,
)
