package com.example.itami_chat.authentication_feature.presentation.register

sealed class RegisterEvent {

    data class OnEmailInputChange(val newValue: String): RegisterEvent()

    data class OnPasswordInputChange(val newValue: String): RegisterEvent()

    data class OnConfirmPasswordInputChange(val newValue: String): RegisterEvent()

    data object OnChangePasswordVisibility: RegisterEvent()

    data object OnChangeConfirmPasswordVisibility: RegisterEvent()

    data object OnRegister: RegisterEvent()

}
