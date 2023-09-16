package com.example.itami_chat.chat_feature.presentation.screens.chat_profile


sealed class ChatProfileUiEvent {

    data object LeftChat : ChatProfileUiEvent()

    data class OnShowSnackbar(val message: String) : ChatProfileUiEvent()

    data object OnNavigateBack : ChatProfileUiEvent()
}
