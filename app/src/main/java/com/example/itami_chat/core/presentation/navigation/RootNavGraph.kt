package com.example.itami_chat.core.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.itami_chat.authentication_feature.presentation.authGraph
import com.example.itami_chat.chat_feature.presentation.chatsGraph
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    startGraphRoute: String = Graph.AUTH_GRAPH,
    navController: NavHostController,
    onShowSnackbar: (message: String) -> Unit,
) {

    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        route = Graph.ROOT,
        startDestination = startGraphRoute
    ) {
        authGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
        chatsGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
    }

}