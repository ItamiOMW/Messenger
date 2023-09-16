package com.example.itami_chat.chat_feature.presentation.screens.add_chat_participants

import com.example.itami_chat.chat_feature.domain.model.ChatParticipant


sealed class AddChatParticipantsUiEvent {

    data class OnNavigateBack(val participants: List<ChatParticipant>?): AddChatParticipantsUiEvent()

    data class OnShowSnackbar(val message: String): AddChatParticipantsUiEvent()

}
