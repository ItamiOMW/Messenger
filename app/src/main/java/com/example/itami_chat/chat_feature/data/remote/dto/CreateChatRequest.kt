package com.example.itami_chat.chat_feature.data.remote.dto

import com.example.itami_chat.chat_feature.domain.model.ChatType

data class CreateChatRequest(
    val name: String?,
    val type: ChatType,
    val participantIds: List<Int>,
)
