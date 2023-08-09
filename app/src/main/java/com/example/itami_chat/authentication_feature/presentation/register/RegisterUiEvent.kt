package com.example.itami_chat.authentication_feature.presentation.register

sealed class RegisterUiEvent {

    data class OnNavigateToVerifyEmail(val email: String): RegisterUiEvent()

}
