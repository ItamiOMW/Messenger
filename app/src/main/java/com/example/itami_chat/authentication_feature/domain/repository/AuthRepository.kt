package com.example.itami_chat.authentication_feature.domain.repository

import com.example.itami_chat.core.domain.model.AppResponse

interface AuthRepository {

    suspend fun login(email: String, password: String): AppResponse<Unit>

    suspend fun register(email: String, password: String): AppResponse<Unit>

    suspend fun authenticate(): AppResponse<Unit>

    suspend fun verifyEmail(email: String, code: Int): AppResponse<Unit>

    suspend fun resendEmailVerificationCode(email: String): AppResponse<Unit>

    suspend fun sendPasswordResetCode(email: String): AppResponse<Unit>

    suspend fun resetPassword(email: String, code: Int, newPassword: String): AppResponse<Unit>

}