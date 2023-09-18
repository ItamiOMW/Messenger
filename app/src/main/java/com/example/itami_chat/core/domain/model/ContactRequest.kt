package com.example.itami_chat.core.domain.model

import com.example.itami_chat.core.utils.Constants

data class ContactRequest(
    val id: Int = Constants.UNKNOWN_ID,
    val sender: SimpleUser,
    val recipient: SimpleUser,
    val status: ContactRequestStatus,
    val createdAt: Long,
)
