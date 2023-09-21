package com.example.itami_chat.settings_feature.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.settings_feature.domain.repository.UserSettingsRepository


class SendPasswordChangeCodeUseCase(private val userSettingsRepository: UserSettingsRepository) {

    suspend operator fun invoke(): AppResponse<Unit> {
        return userSettingsRepository.sendPasswordChangeCode()
    }

}