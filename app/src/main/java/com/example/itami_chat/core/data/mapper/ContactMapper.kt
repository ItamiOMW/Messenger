package com.example.itami_chat.core.data.mapper

import com.example.itami_chat.core.data.remote.dto.response.ContactRequestResponse
import com.example.itami_chat.core.domain.model.ContactRequest


fun ContactRequestResponse.toContactRequest() = ContactRequest(
    id = this.id,
    sender = this.sender.toSimpleUser(),
    recipient = this.recipient.toSimpleUser(),
    status = this.status,
    createdAt = this.createdAt
)