package com.example.itami_chat.chat_feature.domain.model

import com.example.itami_chat.core.domain.model.SimpleUser

data class ChatParticipant(
    val user: SimpleUser,
    val role: ParticipantRole
)
