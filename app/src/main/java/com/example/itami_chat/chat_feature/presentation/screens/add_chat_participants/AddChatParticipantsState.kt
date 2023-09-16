package com.example.itami_chat.chat_feature.presentation.screens.add_chat_participants

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.core.domain.model.SimpleUser


data class AddChatParticipantsState(
    val chat: Chat? = null,
    val contacts: List<SimpleUser> = emptyList(),
    val selectedContacts: List<SimpleUser> = emptyList(),

    val isLoading: Boolean = false,
    val isLoadingContacts: Boolean = false,
    val isSearchFieldVisible: Boolean = false,
)
