package com.example.itami_chat.core.data.remote.dto.response

data class UserProfileResponse(
    val userId: Int,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val isOwnProfile: Boolean,
    val isContact: Boolean,
    val contactRequest: ContactRequestResponse?,
    val canSendMessage: Boolean,
    val isBlockedByMe: Boolean,
    val isBlockedByUser: Boolean,
    val isOnline: Boolean,
    val lastActivity: Long,
)
