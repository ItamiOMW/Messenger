package com.example.itami_chat.chat_feature.di

import com.example.itami_chat.chat_feature.data.remote.ChatApiService
import com.example.itami_chat.chat_feature.data.repository.ChatRepositoryImpl
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.chat_feature.domain.use_case.AddChatParticipantsUseCase
import com.example.itami_chat.chat_feature.domain.use_case.AssignAdminRoleUseCase
import com.example.itami_chat.chat_feature.domain.use_case.CreateChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.DeleteChatParticipantUseCase
import com.example.itami_chat.chat_feature.domain.use_case.DeleteChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.DeleteMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.EditChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.EditMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetChatByIdUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetChatsUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetDialogChatByUsersUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetMessagesForChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.LeaveChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ObserveChatEventsUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ObserveChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ReadMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.RemoveAdminRoleUseCase
import com.example.itami_chat.chat_feature.domain.use_case.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
data object ChatModule {

    @Provides
    @Singleton
    fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl,
    ): ChatRepository = chatRepositoryImpl

    @Provides
    @Singleton
    fun provideChatApiService(
        retrofit: Retrofit,
    ): ChatApiService {
        return retrofit.create(ChatApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAssignAdminRoleUseCase(
        chatRepository: ChatRepository,
    ) = AssignAdminRoleUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideAddChatParticipantsUseCase(
        chatRepository: ChatRepository,
    ) = AddChatParticipantsUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideDeleteChatParticipantUseCase(
        chatRepository: ChatRepository,
    ) = DeleteChatParticipantUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideRemoveAdminRoleUseCase(
        chatRepository: ChatRepository,
    ) = RemoveAdminRoleUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideCreateChatUseCase(
        chatRepository: ChatRepository,
    ) = CreateChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideLeaveChatUseCase(
        chatRepository: ChatRepository,
    ) = LeaveChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideEditChatUseCase(
        chatRepository: ChatRepository,
    ) = EditChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideGetChatsUseCase(
        chatRepository: ChatRepository,
    ) = GetChatsUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideGetChatByIdUseCase(
        chatRepository: ChatRepository,
    ) = GetChatByIdUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideGetDialogChatByUsersUseCase(
        chatRepository: ChatRepository,
    ) = GetDialogChatByUsersUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideGetMessagesForChatUseCase(
        chatRepository: ChatRepository,
    ) = GetMessagesForChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideObserveChatEventsUseCase(
        chatRepository: ChatRepository,
    ) = ObserveChatEventsUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideObserveChatUseCase(
        chatRepository: ChatRepository,
    ) = ObserveChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideSendMessageUseCase(
        chatRepository: ChatRepository,
    ) = SendMessageUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideDeleteChatUseCase(
        chatRepository: ChatRepository,
    ) = DeleteChatUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideDeleteMessageUseCase(
        chatRepository: ChatRepository,
    ) = DeleteMessageUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideEditMessageUseCase(
        chatRepository: ChatRepository,
    ) = EditMessageUseCase(chatRepository)

    @Provides
    @Singleton
    fun provideReadUseCase(
        chatRepository: ChatRepository,
    ) = ReadMessageUseCase(chatRepository)

}