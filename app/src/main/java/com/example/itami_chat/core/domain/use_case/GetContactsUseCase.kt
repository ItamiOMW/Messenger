package com.example.itami_chat.core.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.repository.ContactsRepository

class GetContactsUseCase(private val contactsRepository: ContactsRepository) {

    suspend operator fun invoke(): AppResponse<List<SimpleUser>> {
        return contactsRepository.getContacts()
    }

}