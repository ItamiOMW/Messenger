package com.example.itami_chat.core.data.remote.dto.response

import com.example.itami_chat.core.domain.model.ContactRequestStatus

data class ContactRequestResponse(
    val id: Int,
    val sender: SimpleUserResponse,
    val recipient: SimpleUserResponse,
    val status: ContactRequestStatus,
    val createdAt: Long,
)