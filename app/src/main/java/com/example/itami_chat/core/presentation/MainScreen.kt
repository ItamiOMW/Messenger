package com.example.itami_chat.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.example.itami_chat.core.presentation.navigation.RootNavGraph
import com.example.itami_chat.core.presentation.ui.theme.padding
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    imageLoader: ImageLoader,
) {
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(snackbarHostState) { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        modifier = Modifier
                            .padding(bottom = MaterialTheme.padding.extraLarge),
                        shape = MaterialTheme.shapes.small,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    ) {
        RootNavGraph(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            imageLoader = imageLoader,
            onShowSnackbar = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }

}