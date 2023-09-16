package com.example.itami_chat.chat_feature.presentation.screens.edit_chat

import com.example.itami_chat.chat_feature.domain.model.Chat


sealed class EditChatUiEvent {

    data class OnShowSnackbar(val message: String) : EditChatUiEvent()

    data class OnNavigateBack(val chat: Chat?): EditChatUiEvent()

}
