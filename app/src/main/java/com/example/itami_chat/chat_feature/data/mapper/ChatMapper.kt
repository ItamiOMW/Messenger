package com.example.itami_chat.chat_feature.data.mapper

import com.example.itami_chat.chat_feature.data.remote.dto.ChatParticipantResponse
import com.example.itami_chat.chat_feature.data.remote.dto.ChatResponse
import com.example.itami_chat.chat_feature.data.remote.dto.MessageResponse
import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.core.data.mapper.toSimpleUser


fun MessageResponse.toMessage() = Message(
    id = this.id,
    chatId = this.chatId,
    authorId = this.authorId,
    authorFullName = this.authorFullName,
    authorProfilePictureUrl = this.authorProfilePictureUrl,
    type = this.type,
    text = this.text,
    pictureUrls = this.pictureUrls,
    isRead = this.usersSeenMessage.any { userId -> userId != this.authorId },
    usersSeenMessage = this.usersSeenMessage,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun ChatResponse.toChat() = Chat(
    id = this.id,
    name = this.name ?: this.participants.joinToString(separator = ",") { it.user.fullName },
    type = this.chatType,
    chatPictureUrl = this.chatPictureUrl,
    lastMessage = this.lastMessage?.toMessage(),
    participants = this.participants.map { it.toChatParticipant() },
    unreadMessagesCount = this.unreadMessagesCount,
)

fun ChatParticipantResponse.toChatParticipant() = ChatParticipant(
    user = this.user.toSimpleUser(),
    role = this.role
)