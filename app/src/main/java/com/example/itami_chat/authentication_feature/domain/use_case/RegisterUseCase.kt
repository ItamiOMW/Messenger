package com.example.itami_chat.authentication_feature.domain.use_case

import com.example.itami_chat.authentication_feature.domain.model.RegisterResult
import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.core.domain.util.ValidationUtil

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
    ): RegisterResult {
        val emailError = ValidationUtil.validateEmail(email)
        val passwordError = ValidationUtil.validatePassword(password)
        val confirmPasswordError = ValidationUtil.validatePasswords(password, confirmPassword)

        if (emailError != null || passwordError != null || confirmPasswordError != null) {
            return RegisterResult(emailError, passwordError, confirmPasswordError)
        }

        val response = authRepository.register(email, password)

        return RegisterResult(result = response)
    }

}