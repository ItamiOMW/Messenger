package com.example.itami_chat.chat_feature.presentation.screens.group_chat

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.Message

data class GroupChatState(
    val messages: List<Message> = emptyList(),
    val chat: Chat? = null,
    val me: ChatParticipant? = null,

    val isLoading: Boolean = false,
    val isLoadingNextMessages: Boolean = true,
    val isSendingMessage: Boolean = false,

    val errorMessage: String? = null,

    val messagesPage: Int = 1,
    val endReached: Boolean = false,

    val showLeaveChatDialog: Boolean = false,
    val showDeleteMessageDialog: Boolean = false,
    val messageToDelete: Message? = null,
    val messageToEdit: Message? = null,
)
