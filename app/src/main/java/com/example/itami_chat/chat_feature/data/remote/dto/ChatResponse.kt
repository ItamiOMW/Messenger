package com.example.itami_chat.chat_feature.data.remote.dto

import com.example.itami_chat.chat_feature.domain.model.ChatType

data class ChatResponse(
    val id: Int,
    val name: String? = null,
    val chatType: ChatType,
    val chatPictureUrl: String?,
    val lastMessage: MessageResponse?,
    val participants: List<ChatParticipantResponse>,
    val unreadMessagesCount: Int,
)
