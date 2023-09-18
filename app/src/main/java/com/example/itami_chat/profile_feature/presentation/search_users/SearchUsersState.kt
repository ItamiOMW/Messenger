package com.example.itami_chat.profile_feature.presentation.search_users

import com.example.itami_chat.core.domain.model.SimpleUser

data class SearchUsersState(
    val users: List<SimpleUser> = emptyList(),
    val isLoading: Boolean = false,
)
