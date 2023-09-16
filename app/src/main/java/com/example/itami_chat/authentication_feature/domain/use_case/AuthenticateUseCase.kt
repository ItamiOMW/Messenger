package com.example.itami_chat.authentication_feature.domain.use_case

import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.domain.model.AppResponse

class AuthenticateUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(): AppResponse<Unit> {
        return authRepository.authenticate()
    }

}