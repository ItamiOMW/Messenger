package com.example.itami_chat.chat_feature.presentation.screens.chats



sealed class ChatsEvent {

    data object ChangeDarkModeState : ChatsEvent()

}
