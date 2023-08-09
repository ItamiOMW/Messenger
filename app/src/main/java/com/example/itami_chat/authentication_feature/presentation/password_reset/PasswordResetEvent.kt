package com.example.itami_chat.authentication_feature.presentation.password_reset

sealed class PasswordResetEvent {

    data class OnPasswordInputChange(val newValue: String): PasswordResetEvent()

    data class OnConfirmPasswordInputChange(val newValue: String): PasswordResetEvent()

    data class OnCodeInputChange(val newValue: String): PasswordResetEvent()

    data object OnChangePasswordVisibility: PasswordResetEvent()

    data object OnChangeConfirmPasswordVisibility: PasswordResetEvent()

    data object OnConfirm: PasswordResetEvent()

}
