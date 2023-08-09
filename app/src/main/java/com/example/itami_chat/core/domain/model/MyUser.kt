package com.example.itami_chat.core.domain.model


data class MyUser(
    val id: Int,
    val email: String,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val contactRequestsCount: Int,
    val blockedUsersCount: Int,
    val messagesPermission: MessagesPermission
)
