package com.example.itami_chat.authentication_feature.domain.use_case

import com.example.itami_chat.authentication_feature.domain.model.ResetPasswordResult
import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.domain.util.ValidationUtil

class ResetPasswordUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        email: String,
        code: Int,
        newPassword: String,
        confirmNewPassword: String,
    ): ResetPasswordResult {
        val codeError = ValidationUtil.validateVerificationCode(code)
        val newPasswordError = ValidationUtil.validatePassword(newPassword)
        val confirmNewPasswordError = ValidationUtil.validatePasswords(
            newPassword, confirmNewPassword
        )

        if (codeError != null || newPasswordError != null ||
            confirmNewPasswordError != null
        ) {
            return ResetPasswordResult(
                codeError,
                newPasswordError,
                confirmNewPasswordError
            )
        }

        val response = authRepository.resetPassword(email, code, newPassword)
        return ResetPasswordResult(result = response)

    }

}