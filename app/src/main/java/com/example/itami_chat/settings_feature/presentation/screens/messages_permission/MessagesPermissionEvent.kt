package com.example.itami_chat.settings_feature.presentation.screens.messages_permission

import com.example.itami_chat.core.domain.model.MessagesPermission

sealed class MessagesPermissionEvent {

    data class OnSelectPermission(val permission: MessagesPermission): MessagesPermissionEvent()

    data object OnSave: MessagesPermissionEvent()

}
