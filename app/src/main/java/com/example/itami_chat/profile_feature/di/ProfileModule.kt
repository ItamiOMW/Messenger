package com.example.itami_chat.profile_feature.di

import com.example.itami_chat.core.domain.repository.UserRepository
import com.example.itami_chat.profile_feature.domain.GetUserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(
        userRepository: UserRepository
    ) = GetUserProfileUseCase(userRepository)

}