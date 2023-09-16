package com.example.itami_chat.chat_feature.domain.model

data class Chat(
    val id: Int,
    val name: String,
    val type: ChatType,
    val chatPictureUrl: String?,
    val lastMessage: Message? = null,
    val participants: List<ChatParticipant>,
    val unreadMessagesCount: Int,
)
