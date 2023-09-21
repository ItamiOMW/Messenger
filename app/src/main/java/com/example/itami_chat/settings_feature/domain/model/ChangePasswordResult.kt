package com.example.itami_chat.settings_feature.domain.model

import com.example.itami_chat.core.domain.model.AppResponse

data class ChangePasswordResult(
    val passwordError: Exception? = null,
    val confirmPasswordError: Exception? = null,
    val result: AppResponse<Unit>? = null
)
