package com.example.itami_chat.authentication_feature.data.remote.dto.request

data class VerifyEmailRequest(
    val email: String,
    val code: Int,
)
