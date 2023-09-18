package com.example.itami_chat.settings_feature.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import coil.ImageLoader
import com.example.itami_chat.core.presentation.navigation.Graph
import com.example.itami_chat.core.presentation.navigation.Screen


fun NavGraphBuilder.settingsGraph(
    navController: NavController,
    imageLoader: ImageLoader,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        route = Graph.SETTINGS_GRAPH,
        startDestination = Screen.Settings.fullRoute
    ) {

    }
}