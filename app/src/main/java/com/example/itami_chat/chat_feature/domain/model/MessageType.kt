package com.example.itami_chat.chat_feature.domain.model

enum class MessageType {
    MESSAGE,
    CHAT_CREATED,
    CHAT_NAME_UPDATED,
    CHAT_PICTURE_UPDATED,
    INVITED,
    LEFT,
    KICKED,
    ADMIN_ROLE_ASSIGNED,
    ADMIN_ROLE_REMOVED
}