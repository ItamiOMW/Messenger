package com.example.itami_chat.chat_feature.presentation.screens.add_chat_participants

import com.example.itami_chat.core.domain.model.SimpleUser


sealed class AddChatParticipantsEvent {

    data class OnSearchQueryInputChange(val newValue: String) : AddChatParticipantsEvent()

    data object OnChangeSearchFieldVisibility : AddChatParticipantsEvent()

    data class OnSelectUser(val simpleUser: SimpleUser) : AddChatParticipantsEvent()

    data object OnUpdateContactsList : AddChatParticipantsEvent()

    data object OnAddChatParticipants : AddChatParticipantsEvent()

}
