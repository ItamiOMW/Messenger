package com.example.itami_chat.authentication_feature.domain.use_case

import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.domain.model.AppResponse
import javax.inject.Inject

class ResendEmailVerificationCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String): AppResponse<Unit> {
        return authRepository.resendEmailVerificationCode(email)
    }

}