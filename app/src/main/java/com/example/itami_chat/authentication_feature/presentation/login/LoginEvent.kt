package com.example.itami_chat.authentication_feature.presentation.login

sealed class LoginEvent {

    data class OnEmailInputChange(val newValue: String): LoginEvent()

    data class OnPasswordInputChange(val newValue: String): LoginEvent()

    object OnChangePasswordVisibility: LoginEvent()

    object OnLogin: LoginEvent()

}
