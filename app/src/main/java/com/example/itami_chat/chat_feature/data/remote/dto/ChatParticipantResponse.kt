package com.example.itami_chat.chat_feature.data.remote.dto

import com.example.itami_chat.chat_feature.domain.model.ParticipantRole
import com.example.itami_chat.core.data.remote.dto.response.SimpleUserResponse

data class ChatParticipantResponse(
    val user: SimpleUserResponse,
    val role: ParticipantRole,
)
