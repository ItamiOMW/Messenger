package com.example.itami_chat.settings_feature.presentation.screens.messages_permission

sealed class MessagesPermissionUiEvent {

    data class OnShowSnackbar(val message: String) : MessagesPermissionUiEvent()

    data object OnNavigateBack : MessagesPermissionUiEvent()

}
