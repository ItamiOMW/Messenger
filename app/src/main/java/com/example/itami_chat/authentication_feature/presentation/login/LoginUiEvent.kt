package com.example.itami_chat.authentication_feature.presentation.login

sealed class LoginUiEvent {

    data object OnNavigateToChats: LoginUiEvent()

    data class OnNavigateToVerifyEmail(val email: String): LoginUiEvent()

}
