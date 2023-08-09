package com.example.itami_chat.authentication_feature.domain.use_case

import com.example.itami_chat.authentication_feature.domain.model.LoginResult
import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.domain.util.ValidationUtil
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): LoginResult {

        val emailError = ValidationUtil.validateEmail(email)
        val passwordError = ValidationUtil.validatePassword(password)

        if (emailError != null || passwordError != null) {
            return LoginResult(emailError, passwordError)
        }

        val response = authRepository.login(email, password)

        return LoginResult(result = response)
    }

}