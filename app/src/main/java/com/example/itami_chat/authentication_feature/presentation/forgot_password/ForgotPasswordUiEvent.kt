package com.example.itami_chat.authentication_feature.presentation.forgot_password

sealed class ForgotPasswordUiEvent {

    data class OnPasswordResetCodeSent(val email: String): ForgotPasswordUiEvent()
}
