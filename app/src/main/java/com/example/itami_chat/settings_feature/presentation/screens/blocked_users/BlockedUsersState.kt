package com.example.itami_chat.settings_feature.presentation.screens.blocked_users

import com.example.itami_chat.core.domain.model.SimpleUser

data class BlockedUsersState(
    val blockedUsers: List<SimpleUser> = emptyList(),
    val loadingUsersError: String? = null,
    val isLoading: Boolean = false,
)
