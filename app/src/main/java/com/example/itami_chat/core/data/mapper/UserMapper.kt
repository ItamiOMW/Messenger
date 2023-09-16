package com.example.itami_chat.core.data.mapper

import com.example.itami_chat.authentication_feature.data.remote.dto.response.MyUserResponse
import com.example.itami_chat.core.data.preferences.user.DataStoreUser
import com.example.itami_chat.core.data.remote.dto.response.SimpleUserResponse
import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.model.SimpleUser


fun MyUserResponse.toMyUser() = MyUser(
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

fun DataStoreUser.toMyUser() = MyUser(
    id = this.id,
    email = this.email,
    fullName = this.fullName,
    username = this.username,
    bio = this.bio,
    profilePictureUrl = this.profilePictureUrl,
    contactRequestsCount = this.contactRequestsCount,
    blockedUsersCount = this.blockedUsersCount,
    messagesPermission = this.messagesPermission
)

fun MyUser.toDataStoreUser() = DataStoreUser(
    id = this.id,
    email = this.email,
    fullName = this.fullName,
    username = this.username,
    bio = this.bio,
    profilePictureUrl = this.profilePictureUrl,
    contactRequestsCount = this.contactRequestsCount,
    blockedUsersCount = this.blockedUsersCount,
    messagesPermission = this.messagesPermission
)


fun SimpleUserResponse.toSimpleUser() = SimpleUser(
    id = this.id,
    fullName = this.fullName,
    username = this.username,
    profilePictureUrl = this.profilePictureUrl,
    isOnline = this.isOnline,
    lastActivity = this.lastActivity
)