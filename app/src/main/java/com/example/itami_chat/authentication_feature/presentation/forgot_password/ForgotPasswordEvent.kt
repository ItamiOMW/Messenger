package com.example.itami_chat.authentication_feature.presentation.forgot_password

sealed class ForgotPasswordEvent {

    data class OnEmailInputChange(val newValue: String): ForgotPasswordEvent()

    data object OnConfirm: ForgotPasswordEvent()

}
