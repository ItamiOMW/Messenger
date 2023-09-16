package com.example.itami_chat.chat_feature.presentation.screens.new_group_details

sealed class NewGroupDetailsUiEvent {

    data class OnShowSnackbar(val message: String): NewGroupDetailsUiEvent()

    data object OnChatCreated: NewGroupDetailsUiEvent()

}