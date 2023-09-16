package com.example.itami_chat.core.data.remote.dto.response

data class SimpleUserResponse(
    val id: Int,
    val fullName: String,
    val username: String?,
    val profilePictureUrl: String?,
    val isOnline: Boolean,
    val lastActivity: Long,
)
