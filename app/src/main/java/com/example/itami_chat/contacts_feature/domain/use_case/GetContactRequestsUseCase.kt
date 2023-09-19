package com.example.itami_chat.contacts_feature.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.ContactRequest
import com.example.itami_chat.core.domain.repository.ContactsRepository

class GetContactRequestsUseCase(private val contactsRepository: ContactsRepository) {

    suspend operator fun invoke(): AppResponse<List<ContactRequest>> {
        return contactsRepository.getContactRequests()
    }

}