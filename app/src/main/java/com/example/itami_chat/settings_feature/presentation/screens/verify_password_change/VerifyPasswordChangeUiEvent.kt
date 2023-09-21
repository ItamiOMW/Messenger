package com.example.itami_chat.settings_feature.presentation.screens.verify_password_change

sealed class VerifyPasswordChangeUiEvent {

    data object OnCodeVerified: VerifyPasswordChangeUiEvent()

    data class OnShowSnackbar(val message: String): VerifyPasswordChangeUiEvent()

}
