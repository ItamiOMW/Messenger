package com.example.itami_chat.chat_feature.presentation.screens.new_group_participants

sealed class NewGroupParticipantsUiEvent {

    data class OnShowSnackbar(val message: String): NewGroupParticipantsUiEvent()

}
