package com.example.itami_chat.core.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.repository.ContactsRepository

class AcceptContactRequestUseCase(private val contactsRepository: ContactsRepository) {

    suspend operator fun invoke(requestId: Int): AppResponse<Unit> {
        return contactsRepository.acceptContactRequest(requestId)
    }

}