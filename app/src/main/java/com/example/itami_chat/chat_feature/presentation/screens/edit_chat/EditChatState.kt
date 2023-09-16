package com.example.itami_chat.chat_feature.presentation.screens.edit_chat

import android.net.Uri
import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant


data class EditChatState(
    val chat: Chat? = null,
    val chatPictureUri: Uri? = null,
    val me: ChatParticipant? = null,

    val isLoading: Boolean = false,
    val participantToEdit: ChatParticipant? = null
)
