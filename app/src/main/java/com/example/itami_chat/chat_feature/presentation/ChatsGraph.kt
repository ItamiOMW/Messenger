package com.example.itami_chat.chat_feature.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import com.example.itami_chat.chat_feature.presentation.chats.ChatsScreen
import com.example.itami_chat.chat_feature.presentation.chats.ChatsViewModel
import com.example.itami_chat.core.presentation.navigation.Graph
import com.example.itami_chat.core.presentation.navigation.Screen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.chatsGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        route = Graph.CHATS_GRAPH,
        startDestination = Screen.Chats.fullRoute
    ) {
        composable(Screen.Chats.fullRoute) {
            val viewModel: ChatsViewModel = hiltViewModel()
            ChatsScreen(
                onLogout = {
                    navController.navigate(
                        Graph.AUTH_GRAPH,
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                    navController.graph.setStartDestination(Graph.AUTH_GRAPH)
                },
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent
            )
        }
    }
}