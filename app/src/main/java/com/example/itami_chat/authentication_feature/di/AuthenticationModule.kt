package com.example.itami_chat.authentication_feature.di

import com.example.itami_chat.authentication_feature.data.remote.AuthApiService
import com.example.itami_chat.authentication_feature.data.repository.AuthRepositoryImpl
import com.example.itami_chat.authentication_feature.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthenticationModule {

    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    companion object {

        @Provides
        @Singleton
        fun provideAuthApiService(
            retrofit: Retrofit
        ): AuthApiService {
            return retrofit.create(AuthApiService::class.java)
        }

    }

}