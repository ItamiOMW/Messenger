package com.example.itami_chat.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.itami_chat.core.presentation.navigation.RootNavGraph
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.example.itami_chat.core.presentation.ui.theme.padding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            ItamiChatTheme(theme = mainViewModel.theme, isDarkMode = mainViewModel.isDarkMode) {

                val coroutineScope = rememberCoroutineScope()

                rememberSystemUiController().apply {
                    setStatusBarColor(MaterialTheme.colorScheme.surface)
                    setNavigationBarColor(MaterialTheme.colorScheme.surfaceTint)
                }

                val navController = rememberAnimatedNavController()

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
        }
    }
}

