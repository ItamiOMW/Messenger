package com.example.itami_chat.chat_feature.presentation.chats

sealed class ChatsUiEvent {

    data object OnLogoutSuccessful: ChatsUiEvent()

}
