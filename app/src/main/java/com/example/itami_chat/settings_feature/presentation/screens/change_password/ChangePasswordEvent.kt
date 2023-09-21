package com.example.itami_chat.settings_feature.presentation.screens.change_password

sealed class ChangePasswordEvent {

    data class OnPasswordInputChange(val newValue: String) : ChangePasswordEvent()

    data class OnConfirmPasswordInputChange(val newValue: String) : ChangePasswordEvent()

    data object OnChangePasswordVisibility : ChangePasswordEvent()

    data object OnChangeConfirmPasswordVisibility : ChangePasswordEvent()

    data object OnChangePassword : ChangePasswordEvent()

}
