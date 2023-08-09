package com.example.itami_chat.core.presentation.state

import com.example.itami_chat.core.utils.Constants

data class PasswordTextFieldState(
    val text: String = Constants.EMPTY_STRING,
    val isPasswordVisible: Boolean = false,
    val errorMessage: String? = null
)
