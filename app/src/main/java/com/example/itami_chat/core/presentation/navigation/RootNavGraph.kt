package com.example.itami_chat.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import coil.ImageLoader
import com.example.itami_chat.authentication_feature.presentation.authGraph
import com.example.itami_chat.chat_feature.presentation.chatsGraph
import com.example.itami_chat.contacts_feature.presentation.contactsGraph
import com.example.itami_chat.profile_feature.presentation.profileGraph
import com.example.itami_chat.settings_feature.presentation.settingsGraph


@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    startGraphRoute: String = Graph.AUTH_GRAPH,
    navController: NavHostController,
    imageLoader: ImageLoader,
    onShowSnackbar: (message: String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        route = Graph.ROOT,
        startDestination = startGraphRoute
    ) {
        authGraph(
            navController = navController,
            imageLoader = imageLoader,
            onShowSnackbar = onShowSnackbar
        )
        chatsGraph(
            navController = navController,
            imageLoader = imageLoader,
            onShowSnackbar = onShowSnackbar
        )
        profileGraph(
            navController = navController,
            imageLoader = imageLoader,
            onShowSnackbar = onShowSnackbar
        )
        contactsGraph(
            navController = navController,
            imageLoader = imageLoader,
            onShowSnackbar = onShowSnackbar
        )
        settingsGraph(
            navController = navController,
            imageLoader = imageLoader,
            onShowSnackbar = onShowSnackbar
        )
    }

}