package com.example.itami_chat.settings_feature.presentation.screens.verify_password_change

sealed class VerifyPasswordChangeEvent {

    data object OnVerifyCodeChange : VerifyPasswordChangeEvent()

    data object OnResendCode: VerifyPasswordChangeEvent()

    data class OnCodeValueChange(val newValue: String) : VerifyPasswordChangeEvent()

}
