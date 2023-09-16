package com.example.itami_chat.authentication_feature.domain.use_case

import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.util.ValidationUtil

class SendPasswordResetCodeUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String): AppResponse<Unit> {
        val emailError = ValidationUtil.validateEmail(email)

        if (emailError != null) {
            return AppResponse.failed(emailError)
        }

        return authRepository.sendPasswordResetCode(email)
    }

}