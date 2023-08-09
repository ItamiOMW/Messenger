package com.example.itami_chat.authentication_feature.presentation.login

data class LoginState(
    val isLoggingIn: Boolean = false,
    val error: String? = null
)
