package com.example.itami_chat.contacts_feature.di

import com.example.itami_chat.contacts_feature.domain.use_case.GetContactRequestsUseCase
import com.example.itami_chat.core.domain.repository.ContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContactsModule {

    @Provides
    @Singleton
    fun provideGetContactRequestsUseCase(
        contactsRepository: ContactsRepository
    ) = GetContactRequestsUseCase(contactsRepository)

}