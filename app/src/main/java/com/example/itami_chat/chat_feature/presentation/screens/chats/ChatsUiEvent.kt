package com.example.itami_chat.chat_feature.presentation.screens.chats

sealed class ChatsUiEvent {

    data class OnShowSnackbar(val message: String) : ChatsUiEvent()

}
