package com.example.itami_chat.contacts_feature.presentation.contacts

sealed class ContactsUiEvent {

    data class OnShowSnackbar(val message: String): ContactsUiEvent()

}
