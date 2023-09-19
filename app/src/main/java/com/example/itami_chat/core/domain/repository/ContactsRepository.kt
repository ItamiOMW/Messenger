package com.example.itami_chat.core.domain.repository

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.ContactRequest
import com.example.itami_chat.core.domain.model.SimpleUser

interface ContactsRepository {

    suspend fun getContacts(): AppResponse<List<SimpleUser>>

    suspend fun getContactRequests(): AppResponse<List<ContactRequest>>

    suspend fun deleteContact(userId: Int): AppResponse<Unit>

    suspend fun sendContactRequest(userId: Int): AppResponse<ContactRequest>

    suspend fun acceptContactRequest(requestId: Int): AppResponse<Unit>

    suspend fun declineContactRequest(requestId: Int): AppResponse<Unit>

    suspend fun cancelContactRequest(requestId: Int): AppResponse<Unit>
}