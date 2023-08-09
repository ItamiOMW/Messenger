package com.example.itami_chat.authentication_feature.data.remote.dto.response

data class MyUserResponse(
    val id: Int,
    val email: String,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val blockedUsersCount: Int,
    val contactRequestsCount: Int,
    val messagesPermission: String,
    val profilePictureUrl: String?,
)