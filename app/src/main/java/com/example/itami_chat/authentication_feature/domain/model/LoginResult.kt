package com.example.itami_chat.authentication_feature.domain.model

import com.example.itami_chat.core.domain.model.AppResponse

data class LoginResult(
    val emailError: Exception? = null,
    val passwordError: Exception? = null,
    val result: AppResponse<Unit>? = null,
)
