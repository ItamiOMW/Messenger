package com.example.itami_chat.core.di

import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.preferences.auth.EncryptedAuthManager
import com.example.itami_chat.core.data.preferences.settings.DataStoreAppSettingsManager
import com.example.itami_chat.core.data.preferences.user.DataStoreUserManager
import com.example.itami_chat.core.data.preferences.user.UserManager
import com.example.itami_chat.core.data.remote.ApiConstants
import com.example.itami_chat.core.data.remote.service.UserApiService
import com.example.itami_chat.core.data.repository.UserRepositoryImpl
import com.example.itami_chat.core.domain.preferences.AppSettingsManager
import com.example.itami_chat.core.domain.repository.UserRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {


    @Provides
    @Singleton
    fun provideUserApiService(
        retrofit: Retrofit,
    ): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideAppSettingsManager(
        appSettingsManager: DataStoreAppSettingsManager,
    ): AppSettingsManager = appSettingsManager


    @Singleton
    @Provides
    fun provideAuthManager(
        authManager: EncryptedAuthManager,
    ): AuthManager = authManager


    @Singleton
    @Provides
    fun provideUserManager(
        userManager: DataStoreUserManager,
    ): UserManager = userManager

    @Singleton
    @Provides
    fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository = userRepositoryImpl


    @Singleton
    @Provides
    fun provideApiRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

}