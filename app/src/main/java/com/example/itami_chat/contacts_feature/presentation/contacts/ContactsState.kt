package com.example.itami_chat.contacts_feature.presentation.contacts

import com.example.itami_chat.core.domain.model.ContactRequest
import com.example.itami_chat.core.domain.model.SimpleUser

data class ContactsState(
    val contacts: List<SimpleUser> = emptyList(),
    val myContactRequests: List<ContactRequest> = emptyList(),
    val contactRequests: List<ContactRequest> = emptyList(),

    val isLoading: Boolean = false,
    val isLoadingContacts: Boolean = false,
    val isLoadingContactRequests: Boolean = false,
    val isSearchFieldVisible: Boolean = false,
)
