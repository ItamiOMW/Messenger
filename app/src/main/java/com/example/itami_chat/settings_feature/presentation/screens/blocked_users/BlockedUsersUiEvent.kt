package com.example.itami_chat.settings_feature.presentation.screens.blocked_users

sealed class BlockedUsersUiEvent {

    data class OnShowSnackbar(val message: String): BlockedUsersUiEvent()

}
