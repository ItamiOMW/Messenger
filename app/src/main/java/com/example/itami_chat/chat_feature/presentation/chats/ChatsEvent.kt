package com.example.itami_chat.chat_feature.presentation.chats

sealed class ChatsEvent {

    data object OnLogout: ChatsEvent()

}
