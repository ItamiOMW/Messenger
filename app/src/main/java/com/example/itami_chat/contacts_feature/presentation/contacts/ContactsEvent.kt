package com.example.itami_chat.contacts_feature.presentation.contacts

sealed class ContactsEvent {

    data class OnSearchContactsQueryChange(val newValue: String) : ContactsEvent()

    data object OnChangeSearchFieldVisibility : ContactsEvent()

    data class OnDeleteContact(val id: Int) : ContactsEvent()

    data class OnAcceptContactRequest(val id: Int) : ContactsEvent()

    data class OnDeclineContactRequest(val id: Int) : ContactsEvent()

    data class OnCancelContactRequest(val id: Int) : ContactsEvent()

    data object OnRefreshContacts : ContactsEvent()

    data object OnRefreshContactRequests : ContactsEvent()

}
