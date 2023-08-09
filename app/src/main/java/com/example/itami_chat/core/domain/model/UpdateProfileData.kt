package com.example.itami_chat.core.domain.model

data class UpdateProfileData(
    val fullName: String,
    val username: String?,
    val bio: String?,
)
