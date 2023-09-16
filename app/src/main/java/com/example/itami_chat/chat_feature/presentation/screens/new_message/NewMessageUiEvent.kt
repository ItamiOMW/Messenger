package com.example.itami_chat.chat_feature.presentation.screens.new_message

sealed class NewMessageUiEvent {

    data class OnShowSnackbar(val message: String): NewMessageUiEvent()

}
