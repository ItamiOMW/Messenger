package com.example.itami_chat.chat_feature.presentation.screens.group_chat

import com.example.itami_chat.chat_feature.domain.model.Message

sealed class GroupChatUiEvent {

    data class NewMessage(val message: Message): GroupChatUiEvent()

    data object MessagePageLoaded: GroupChatUiEvent()

    data class OnShowSnackbar(val message: String): GroupChatUiEvent()

    data object OnNavigateBack: GroupChatUiEvent()

}
