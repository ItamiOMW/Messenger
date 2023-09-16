package com.example.itami_chat.chat_feature.data.remote.dto

import com.example.itami_chat.chat_feature.domain.model.MessageType

data class SendMessageRequest(
    val chatId: Int? = null,
    val toId: Int? = null,
    val text: String? = null,
    val type: MessageType,
)