package com.example.itami_chat.chat_feature.data.remote.dto

import com.example.itami_chat.chat_feature.domain.model.MessageType

data class MessageResponse(
    val id: Int,
    val chatId: Int,
    val authorId: Int,
    val authorFullName: String,
    val authorProfilePictureUrl: String?,
    val type: MessageType,
    val text: String?,
    val pictureUrls: List<String>?,
    val usersSeenMessage: List<Int>,
    val createdAt: Long,
    val updatedAt: Long?,
)
