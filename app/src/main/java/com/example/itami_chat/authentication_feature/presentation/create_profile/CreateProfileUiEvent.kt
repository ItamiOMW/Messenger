package com.example.itami_chat.authentication_feature.presentation.create_profile

sealed class CreateProfileUiEvent {

    data object OnNavigateToChats: CreateProfileUiEvent()

}
