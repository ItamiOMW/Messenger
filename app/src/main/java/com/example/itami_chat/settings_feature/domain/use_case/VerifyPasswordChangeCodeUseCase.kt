package com.example.itami_chat.settings_feature.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.util.ValidationUtil
import com.example.itami_chat.settings_feature.domain.repository.UserSettingsRepository


class VerifyPasswordChangeCodeUseCase(private val userSettingsRepository: UserSettingsRepository) {

    suspend operator fun invoke(code: Int): AppResponse<Unit> {
        val codeError = ValidationUtil.validateVerificationCode(code)

        if (codeError != null) {
            return AppResponse.failed(codeError)
        }

        return userSettingsRepository.verifyPasswordChangeCode(code)
    }

}