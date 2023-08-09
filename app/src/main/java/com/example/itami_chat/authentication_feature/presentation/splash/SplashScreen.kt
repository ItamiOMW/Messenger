package com.example.itami_chat.authentication_feature.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow


@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    onNavigateToChats: () -> Unit,
    uiEvent: Flow<SplashUiEvent>,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is SplashUiEvent.Authenticated -> onNavigateToChats()

                is SplashUiEvent.NotAuthenticated -> onNavigateToOnboarding()

                is SplashUiEvent.OnShowSnackbar -> onShowSnackbar(event.message)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Temporary splash screen.",
            style = MaterialTheme.typography.displaySmall
        )
    }

}