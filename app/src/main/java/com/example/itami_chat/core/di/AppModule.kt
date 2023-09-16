package com.example.itami_chat.core.di

import android.content.Context
import coil.ImageLoader
import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.preferences.auth.EncryptedAuthManager
import com.example.itami_chat.core.data.preferences.settings.DataStoreAppSettingsManager
import com.example.itami_chat.core.data.preferences.user.DataStoreUserManager
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.data.remote.ApiConstants
import com.example.itami_chat.core.data.remote.service.ContactsApiService
import com.example.itami_chat.core.data.remote.service.UserApiService
import com.example.itami_chat.core.data.repository.ContactsRepositoryImpl
import com.example.itami_chat.core.data.repository.UserRepositoryImpl
import com.example.itami_chat.core.domain.preferences.AppSettingsManager
import com.example.itami_chat.core.domain.repository.ContactsRepository
import com.example.itami_chat.core.domain.repository.UserRepository
import com.example.itami_chat.core.domain.usecase.GetContactsUseCase
import com.example.itami_chat.core.domain.usecase.GetUsersByIdsUseCase
import com.example.itami_chat.core.domain.usecase.UpdateProfileUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideGetContactsUseCase(
        contactsRepository: ContactsRepository
    ) = GetContactsUseCase(contactsRepository)

    @Provides
    @Singleton
    fun provideGetUsersByIdsUseCase(
        userRepository: UserRepository
    ) = GetUsersByIdsUseCase(userRepository)

    @Provides
    @Singleton
    fun provideUpdateProfileUseCase(
        userRepository: UserRepository
    ) = UpdateProfileUseCase(userRepository)

    @Provides
    @Singleton
    fun provideUserApiService(
        retrofit: Retrofit,
    ): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContactsApiService(
        retrofit: Retrofit,
    ): ContactsApiService {
        return retrofit.create(ContactsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideAppSettingsManager(
        appSettingsManager: DataStoreAppSettingsManager,
    ): AppSettingsManager = appSettingsManager

    @Provides
    @Singleton
    fun provideAuthManager(
        authManager: EncryptedAuthManager,
    ): AuthManager = authManager

    @Provides
    @Singleton
    fun provideUserManager(
        userManager: DataStoreUserManager,
    ): UserManager = userManager

    @Provides
    @Singleton
    fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository = userRepositoryImpl

    @Provides
    @Singleton
    fun provideContactsRepository(
        contactsRepositoryImpl: ContactsRepositoryImpl
    ): ContactsRepository = contactsRepositoryImpl


    @Provides
    @Singleton fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
        }
    }

    @Provides
    @Singleton
    fun provideApiRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader = ImageLoader.Builder(context)
        .crossfade(true)
        .build()


}