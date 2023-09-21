package com.example.itami_chat.settings_feature.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.settings_feature.domain.repository.UserSettingsRepository

class ChangeMessagesPermissionUseCase(private val userSettingsRepository: UserSettingsRepository) {

    suspend operator fun invoke(permission: MessagesPermission): AppResponse<Unit> {
        return userSettingsRepository.changeMessagesPermission(permission)
    }

}