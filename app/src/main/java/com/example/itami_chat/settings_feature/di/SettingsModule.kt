package com.example.itami_chat.settings_feature.di

import com.example.itami_chat.settings_feature.data.remote.UserSettingsApiService
import com.example.itami_chat.settings_feature.data.repository.UserSettingsRepositoryImpl
import com.example.itami_chat.settings_feature.domain.repository.UserSettingsRepository
import com.example.itami_chat.settings_feature.domain.use_case.ChangeMessagesPermissionUseCase
import com.example.itami_chat.settings_feature.domain.use_case.ChangePasswordUseCase
import com.example.itami_chat.settings_feature.domain.use_case.DeleteAccountUseCase
import com.example.itami_chat.settings_feature.domain.use_case.GetBlockedUsersUseCase
import com.example.itami_chat.settings_feature.domain.use_case.SendPasswordChangeCodeUseCase
import com.example.itami_chat.settings_feature.domain.use_case.VerifyPasswordChangeCodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideUserSettingApiService(
        retrofit: Retrofit,
    ): UserSettingsApiService {
        return retrofit.create(UserSettingsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChangeMessagesPermissionUseCase(
        userSettingsRepository: UserSettingsRepository,
    ) = ChangeMessagesPermissionUseCase(userSettingsRepository)

    @Provides
    @Singleton
    fun provideGetBlockedUsersUseCase(
        userSettingsRepository: UserSettingsRepository,
    ) = GetBlockedUsersUseCase(userSettingsRepository)

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(
        userSettingsRepository: UserSettingsRepository,
    ) = DeleteAccountUseCase(userSettingsRepository)

    @Provides
    @Singleton
    fun provideChangePasswordCodeUseCase(
        userSettingsRepository: UserSettingsRepository,
    ) = ChangePasswordUseCase(userSettingsRepository)

    @Provides
    @Singleton
    fun provideVerifyPasswordChangeCodeUseCase(
        userSettingsRepository: UserSettingsRepository,
    ) = VerifyPasswordChangeCodeUseCase(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSendPasswordChangeCodeUseCase(
        userSettingsRepository: UserSettingsRepository,
    ) = SendPasswordChangeCodeUseCase(userSettingsRepository)

    @Provides
    @Singleton
    fun provideUserSettingsRepository(
        userSettingsRepositoryImpl: UserSettingsRepositoryImpl,
    ): UserSettingsRepository = userSettingsRepositoryImpl

}