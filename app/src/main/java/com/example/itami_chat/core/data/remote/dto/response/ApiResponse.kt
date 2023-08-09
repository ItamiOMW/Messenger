package com.example.itami_chat.core.data.remote.dto.response

data class ApiResponse<T>(
    val message: String? = null,
    val data: T? = null
)
