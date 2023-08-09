package com.example.itami_chat.core.domain.model

data class ProfileValidationResult(
    val fullNameError: Exception? = null,
    val usernameError: Exception? = null,
    val bioError: Exception? = null,
    val response: AppResponse<Unit>? = null,
)
