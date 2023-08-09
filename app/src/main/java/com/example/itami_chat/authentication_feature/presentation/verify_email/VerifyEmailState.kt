package com.example.itami_chat.authentication_feature.presentation.verify_email

data class VerifyEmailState(
    val codeValue: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
)
