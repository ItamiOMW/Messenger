package com.example.itami_chat.chat_feature.presentation.screens.new_message

sealed class NewMessageEvent {

    data class OnSearchQueryInputChange(val newValue: String): NewMessageEvent()

    data object OnChangeSearchFieldVisibility: NewMessageEvent()

    data object OnUpdateContactsList: NewMessageEvent()

}
