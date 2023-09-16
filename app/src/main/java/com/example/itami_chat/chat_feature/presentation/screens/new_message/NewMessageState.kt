package com.example.itami_chat.chat_feature.presentation.screens.new_message

import com.example.itami_chat.core.domain.model.SimpleUser

data class NewMessageState(
    val isSearchFieldVisible: Boolean = false,
    val isLoadingContacts: Boolean = false,
    val contacts: List<SimpleUser> = emptyList(),
)
