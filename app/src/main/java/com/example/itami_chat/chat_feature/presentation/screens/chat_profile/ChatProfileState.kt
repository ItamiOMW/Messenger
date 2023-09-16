package com.example.itami_chat.chat_feature.presentation.screens.chat_profile

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant


data class ChatProfileState(
    val chat: Chat? = null,
    val me: ChatParticipant? = null,
    val isLoading: Boolean = false,

    val showLeaveChatDialog: Boolean = false,
)
