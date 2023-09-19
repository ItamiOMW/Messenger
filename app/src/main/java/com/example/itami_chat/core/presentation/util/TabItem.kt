package com.example.itami_chat.core.presentation.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

data class TabItem(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int? = null,
    val content: @Composable () -> Unit,
)