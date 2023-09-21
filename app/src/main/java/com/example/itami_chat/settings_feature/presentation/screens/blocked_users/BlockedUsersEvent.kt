package com.example.itami_chat.settings_feature.presentation.screens.blocked_users

sealed class BlockedUsersEvent {

    data class OnUnblockUser(val id: Int): BlockedUsersEvent()

    data object OnReloadBlockedUsers: BlockedUsersEvent()

}
