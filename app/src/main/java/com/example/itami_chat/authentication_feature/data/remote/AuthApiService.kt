package com.example.itami_chat.authentication_feature.data.remote

import com.example.itami_chat.authentication_feature.data.remote.dto.request.LoginRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.PasswordResetRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.RegisterRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.SendPasswordResetCodeRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.SendVerificationCodeRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.request.VerifyEmailRequest
import com.example.itami_chat.authentication_feature.data.remote.dto.response.AuthResponse
import com.example.itami_chat.authentication_feature.data.remote.dto.response.MyUserResponse
import com.example.itami_chat.core.data.remote.dto.response.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body loginCredentials: LoginRequest,
    ): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body registerCredentials: RegisterRequest,
    ): Response<ApiResponse<Unit>>

    @GET("api/v1/auth/authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String,
    ): Response<ApiResponse<MyUserResponse>>

    @POST("api/v1/auth/verify-email")
    suspend fun verifyEmail(
        @Body verifyEmailRequest: VerifyEmailRequest,
    ): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/auth/send-verification-code")
    suspend fun sendEmailVerificationCode(
        @Body sendVerificationCodeRequest: SendVerificationCodeRequest,
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/auth/send-password-reset-code")
    suspend fun sendPasswordResetCode(
        @Body sendPasswordResetCodeRequest: SendPasswordResetCodeRequest
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(
        @Body passwordResetRequest: PasswordResetRequest
    ): Response<ApiResponse<Unit>>

}