package com.example.itami_chat.chat_feature.presentation.screens.chats

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.core.domain.model.MyUser

data class ChatsState(
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = false,
    val myUser: MyUser? = null,
    val isDarkMode: Boolean = false,
    val error: String? = null
)
