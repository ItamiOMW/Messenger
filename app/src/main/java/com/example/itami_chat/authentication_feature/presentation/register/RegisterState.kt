package com.example.itami_chat.authentication_feature.presentation.register

data class RegisterState(
    val isRegistering: Boolean = false,
    val error: String? = null
)
