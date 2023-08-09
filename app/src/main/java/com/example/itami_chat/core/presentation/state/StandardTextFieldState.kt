package com.example.itami_chat.core.presentation.state

import com.example.itami_chat.core.utils.Constants

data class StandardTextFieldState(
    val text: String = Constants.EMPTY_STRING,
    val errorMessage: String? = null
)
