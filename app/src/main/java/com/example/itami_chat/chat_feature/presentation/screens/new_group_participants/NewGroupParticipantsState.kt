package com.example.itami_chat.chat_feature.presentation.screens.new_group_participants

import com.example.itami_chat.core.domain.model.SimpleUser

data class NewGroupParticipantsState(
    val contacts: List<SimpleUser> = emptyList(),
    val selectedContacts: List<SimpleUser> = emptyList(),

    val isLoadingContacts: Boolean = false,
    val isSearchFieldVisible: Boolean = false,
)
