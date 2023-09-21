package com.example.itami_chat.settings_feature.domain.repository

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.domain.model.SimpleUser

interface UserSettingsRepository {

    suspend fun sendPasswordChangeCode(): AppResponse<Unit>

    suspend fun verifyPasswordChangeCode(code: Int): AppResponse<Unit>

    suspend fun changePassword(newPassword: String): AppResponse<Unit>

    suspend fun changeMessagesPermission(permission: MessagesPermission): AppResponse<Unit>

    suspend fun deleteAccount(): AppResponse<Unit>

    suspend fun getBlockedUsers(): AppResponse<List<SimpleUser>>

}