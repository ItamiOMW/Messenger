package com.example.itami_chat.authentication_feature.presentation.verify_email

sealed class VerifyEmailEvent {

    data class OnCodeInputChange(val newValue: String): VerifyEmailEvent()

    data object OnConfirm: VerifyEmailEvent()

    data object OnResendCode: VerifyEmailEvent()

}
