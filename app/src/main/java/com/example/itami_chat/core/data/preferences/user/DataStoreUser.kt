package com.example.itami_chat.core.data.preferences.user

import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.utils.Constants
import kotlinx.serialization.Serializable

@Serializable
data class DataStoreUser(
    val id: Int = Constants.UNKNOWN_ID,
    val email: String = Constants.EMPTY_STRING,
    val fullName: String = Constants.EMPTY_STRING,
    val username: String? = null,
    val bio: String? = null,
    val profilePictureUrl: String? = null,
    val contactRequestsCount: Int = 0,
    val blockedUsersCount: Int = 0,
    val messagesPermission: MessagesPermission = MessagesPermission.ANYONE
)
