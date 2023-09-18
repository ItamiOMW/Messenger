package com.example.itami_chat.core.domain.model

data class UserProfile(
    val userId: Int,
    val fullName: String,
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?,
    val isOwnProfile: Boolean,
    val isContact: Boolean,
    val contactRequest: ContactRequest?,
    val canSendMessage: Boolean,
    val isBlockedByMe: Boolean,
    val isBlockedByUser: Boolean,
    val isOnline: Boolean,
    val lastActivity: Long,
)
