package com.example.itami_chat.authentication_feature.domain.use_case

import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.util.ValidationUtil

class VerifyEmailUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String, code: Int): AppResponse<Unit> {
        val codeError = ValidationUtil.validateVerificationCode(code)

        if (codeError != null) {
            return AppResponse.failed(codeError)
        }

        return authRepository.verifyEmail(email, code)
    }

}