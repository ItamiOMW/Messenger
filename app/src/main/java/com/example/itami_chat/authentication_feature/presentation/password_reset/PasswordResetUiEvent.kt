package com.example.itami_chat.authentication_feature.presentation.password_reset

sealed class PasswordResetUiEvent {

    data object OnPasswordResetSuccessful: PasswordResetUiEvent()

    data class OnShowSnackbar(val message: String): PasswordResetUiEvent()

}
