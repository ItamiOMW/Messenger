package com.example.itami_chat.core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Spacing(
    val default: Dp = 0.dp,
    val extraSmall: Dp = 6.dp,
    val small: Dp = 10.dp,
    val medium: Dp = 20.dp,
    val extraMedium: Dp = 30.dp,
    val large: Dp = 50.dp,
    val extraLarge: Dp = 72.dp,
    val enormous: Dp = 80.dp,
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current