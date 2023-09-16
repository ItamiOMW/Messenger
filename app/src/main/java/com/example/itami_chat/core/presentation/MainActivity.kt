package com.example.itami_chat.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.example.itami_chat.core.presentation.ui.theme.ItamiChatTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme = viewModel.theme
            val isDarkMode = viewModel.isDarkMode
            ItamiChatTheme(theme = theme, isDarkMode = isDarkMode) {
                rememberSystemUiController().apply {
                    setStatusBarColor(MaterialTheme.colorScheme.surface)
                    setNavigationBarColor(MaterialTheme.colorScheme.surfaceTint)
                }

                val navController = rememberNavController()

                MainScreen(
                    mainViewModel = viewModel,
                    imageLoader = imageLoader,
                    navController = navController
                )
            }
        }
    }
}

