package com.example.itami_chat.core.domain.repository

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.SimpleUser

interface ContactsRepository {
    suspend fun getContacts(): AppResponse<List<SimpleUser>>

}