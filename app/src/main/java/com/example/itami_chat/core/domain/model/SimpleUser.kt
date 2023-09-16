package com.example.itami_chat.core.domain.model

data class SimpleUser(
    val id: Int,
    val fullName: String,
    val username: String?,
    val profilePictureUrl: String?,
    val isOnline: Boolean,
    val lastActivity: Long,
)
