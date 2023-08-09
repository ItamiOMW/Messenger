package com.example.itami_chat.authentication_feature.presentation.password_reset

data class PasswordResetState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
