package com.example.itami_chat.chat_feature.presentation.screens.dialog_chat

import com.example.itami_chat.chat_feature.domain.model.Message

sealed class DialogChatUiEvent {

    data class NewMessage(val message: Message): DialogChatUiEvent()

    data object MessagePageLoaded: DialogChatUiEvent()

    data class OnShowSnackbar(val message: String): DialogChatUiEvent()

    data class ChatDeleted(val message: String): DialogChatUiEvent()

}
