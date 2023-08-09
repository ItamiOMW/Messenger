package com.example.itami_chat.core.data.mapper

import com.example.itami_chat.authentication_feature.data.remote.dto.response.MyUserResponse
import com.example.itami_chat.core.data.preferences.user.DataStoreUser
import com.example.itami_chat.core.domain.model.MessagesPermission


fun MyUserResponse.toDataStoreUser() = DataStoreUser(
    id = this.id,
    email = this.email,
    fullName = this.fullName,
    username = this.username,
    bio = this.bio,
    profilePictureUrl = this.profilePictureUrl,
    contactRequestsCount = this.contactRequestsCount,
    blockedUsersCount = this.blockedUsersCount,
    messagesPermission = MessagesPermission.valueOf(this.messagesPermission)
)