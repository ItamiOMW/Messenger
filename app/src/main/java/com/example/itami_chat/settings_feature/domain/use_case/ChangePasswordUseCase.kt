package com.example.itami_chat.settings_feature.domain.use_case

import com.example.itami_chat.core.domain.util.ValidationUtil
import com.example.itami_chat.settings_feature.domain.model.ChangePasswordResult
import com.example.itami_chat.settings_feature.domain.repository.UserSettingsRepository

class ChangePasswordUseCase(private val userSettingsRepository: UserSettingsRepository) {

    suspend operator fun invoke(password: String, confirmPassword: String): ChangePasswordResult {
        val passwordError = ValidationUtil.validatePassword(password)
        val confirmPasswordError = ValidationUtil.validatePasswords(password, confirmPassword)

        if (passwordError != null || confirmPasswordError != null) {
            return ChangePasswordResult(passwordError, confirmPasswordError)
        }

        val response = userSettingsRepository.changePassword(password)
        return ChangePasswordResult(result = response)
    }

}