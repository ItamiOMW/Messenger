package com.example.itami_chat.authentication_feature.di

import com.example.itami_chat.authentication_feature.data.remote.AuthApiService
import com.example.itami_chat.authentication_feature.data.repository.AuthRepositoryImpl
import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import com.example.itami_chat.authentication_feature.domain.use_case.AuthenticateUseCase
import com.example.itami_chat.authentication_feature.domain.use_case.LoginUseCase
import com.example.itami_chat.authentication_feature.domain.use_case.RegisterUseCase
import com.example.itami_chat.authentication_feature.domain.use_case.ResendEmailVerificationCodeUseCase
import com.example.itami_chat.authentication_feature.domain.use_case.ResetPasswordUseCase
import com.example.itami_chat.authentication_feature.domain.use_case.SendPasswordResetCodeUseCase
import com.example.itami_chat.authentication_feature.domain.use_case.VerifyEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
data object AuthenticationModule {

    @Provides
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideAuthApiService(
        retrofit: Retrofit,
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun bindAuthenticateUseCase(
        authRepository: AuthRepository,
    ): AuthenticateUseCase = AuthenticateUseCase(authRepository)

    @Provides
    @Singleton
    fun bindLoginUseCase(
        authRepository: AuthRepository,
    ): LoginUseCase = LoginUseCase(authRepository)

    @Provides
    @Singleton
    fun bindRegisterUseCase(
        authRepository: AuthRepository,
    ): RegisterUseCase = RegisterUseCase(authRepository)

    @Provides
    @Singleton
    fun bindResendEmailVerificationUseCase(
        authRepository: AuthRepository,
    ): ResendEmailVerificationCodeUseCase = ResendEmailVerificationCodeUseCase(authRepository)

    @Provides
    @Singleton
    fun bindResetPasswordUseCase(
        authRepository: AuthRepository,
    ): ResetPasswordUseCase = ResetPasswordUseCase(authRepository)

    @Provides
    @Singleton
    fun bindSendPasswordResetCodeUseCase(
        authRepository: AuthRepository,
    ): SendPasswordResetCodeUseCase = SendPasswordResetCodeUseCase(authRepository)

    @Provides
    @Singleton
    fun bindVerifyEmailUseCase(
        authRepository: AuthRepository,
    ): VerifyEmailUseCase = VerifyEmailUseCase(authRepository)

}