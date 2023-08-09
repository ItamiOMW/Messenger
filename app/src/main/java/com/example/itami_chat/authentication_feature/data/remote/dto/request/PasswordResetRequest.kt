package com.example.itami_chat.authentication_feature.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val email: String,
    val passwordResetCode: Int,
    val newPassword: String,
)
