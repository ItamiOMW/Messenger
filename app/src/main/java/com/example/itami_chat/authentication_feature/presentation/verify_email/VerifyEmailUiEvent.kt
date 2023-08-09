package com.example.itami_chat.authentication_feature.presentation.verify_email

sealed class VerifyEmailUiEvent {

    data object OnEmailVerified : VerifyEmailUiEvent()

    data class OnShowSnackbar(val message: String) : VerifyEmailUiEvent()

}
