package com.example.itami_chat.core.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.itami_chat.core.domain.model.Theme

private val DefaultDarkColorScheme = darkColorScheme(
    primary = PurpleBlue80,
    onPrimary = White90,
    surface = Black10,
    onSurface = White85,
    surfaceVariant = Black10,
    onSurfaceVariant = White80,
    inverseSurface = Black5,
    inverseOnSurface = White80,
    surfaceTint = Black11,
    outlineVariant = Black10,
    background = Black5,
    onBackground = White80
)

private val DefaultLightColorScheme = lightColorScheme(
    primary = PurpleBlue90,
    onPrimary = White90,
    surface = WhitePurplyBlue,
    onSurface = Black100,
    surfaceVariant = WhitePurplyBlue,
    onSurfaceVariant = Black100,
    inverseSurface = White100,
    inverseOnSurface = Black100,
    surfaceTint = White85,
    outlineVariant = White75,
    background = White100,
    onBackground = Black100
)

private val BlueDarkColorScheme = lightColorScheme(
    primary = PurpleBlue65,
    onPrimary = White90,
    surface = DarkPurplyBlue20,
    onSurface = White80,
    surfaceVariant = DarkPurplyBlue20,
    onSurfaceVariant = White80,
    inverseSurface = DarkPurplyBlue10,
    inverseOnSurface = White75,
    surfaceTint = BlackPurplyBlue,
    outlineVariant = BlackBlue,
    background = DarkPurplyBlue10,
    onBackground = White80
)

private val BlueLightColorScheme = lightColorScheme(
    primary = Blue70,
    onPrimary = White100,
    surface = Blue60,
    onSurface = White90,
    surfaceVariant = White85,
    onSurfaceVariant = Black100,
    inverseSurface = White100,
    inverseOnSurface = Black100,
    surfaceTint = White85,
    outlineVariant = White75,
    background = White100,
    onBackground = Black100
)


@Composable
fun ItamiChatTheme(
    theme: Theme = Theme.Default,
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colorScheme = if (isDarkMode) {
        when (theme) {
            Theme.Default -> DefaultDarkColorScheme
            Theme.Blue -> BlueDarkColorScheme
        }
    } else {
        when (theme) {
            Theme.Default -> DefaultLightColorScheme
            Theme.Blue -> BlueLightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}