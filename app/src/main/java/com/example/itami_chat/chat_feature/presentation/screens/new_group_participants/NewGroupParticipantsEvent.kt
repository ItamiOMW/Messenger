package com.example.itami_chat.chat_feature.presentation.screens.new_group_participants

import com.example.itami_chat.core.domain.model.SimpleUser

sealed class NewGroupParticipantsEvent {

    data class OnSearchQueryInputChange(val newValue: String) : NewGroupParticipantsEvent()

    data object OnChangeSearchFieldVisibility : NewGroupParticipantsEvent()

    data class OnSelectUser(val simpleUser: SimpleUser) : NewGroupParticipantsEvent()

    data object OnUpdateContactsList : NewGroupParticipantsEvent()

}
