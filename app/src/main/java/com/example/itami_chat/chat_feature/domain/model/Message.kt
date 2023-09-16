package com.example.itami_chat.chat_feature.domain.model

data class Message(
    val id: Int,
    val chatId: Int,
    val authorId: Int,
    val authorFullName: String,
    val authorProfilePictureUrl: String?,
    val type: MessageType,
    val text: String? = null,
    val pictureUrls: List<String>? = null,
    val isRead: Boolean,
    val usersSeenMessage: List<Int>,
    val createdAt: Long,
    val updatedAt: Long?,
)
