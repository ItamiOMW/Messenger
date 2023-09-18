package com.example.itami_chat.chat_feature.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import coil.ImageLoader
import com.example.itami_chat.chat_feature.presentation.screens.add_chat_participants.AddChatParticipantsScreen
import com.example.itami_chat.chat_feature.presentation.screens.add_chat_participants.AddChatParticipantsViewModel
import com.example.itami_chat.chat_feature.presentation.screens.chat_profile.ChatProfileScreen
import com.example.itami_chat.chat_feature.presentation.screens.chat_profile.ChatProfileViewModel
import com.example.itami_chat.chat_feature.presentation.screens.chats.ChatsScreen
import com.example.itami_chat.chat_feature.presentation.screens.chats.ChatsViewModel
import com.example.itami_chat.chat_feature.presentation.screens.dialog_chat.DialogChatScreen
import com.example.itami_chat.chat_feature.presentation.screens.dialog_chat.DialogChatViewModel
import com.example.itami_chat.chat_feature.presentation.screens.edit_chat.EditChatScreen
import com.example.itami_chat.chat_feature.presentation.screens.edit_chat.EditChatViewModel
import com.example.itami_chat.chat_feature.presentation.screens.group_chat.GroupChatScreen
import com.example.itami_chat.chat_feature.presentation.screens.group_chat.GroupChatViewModel
import com.example.itami_chat.chat_feature.presentation.screens.new_group_details.NewGroupDetailsScreen
import com.example.itami_chat.chat_feature.presentation.screens.new_group_details.NewGroupDetailsViewModel
import com.example.itami_chat.chat_feature.presentation.screens.new_group_participants.NewGroupParticipantsScreen
import com.example.itami_chat.chat_feature.presentation.screens.new_group_participants.NewGroupParticipantsViewModel
import com.example.itami_chat.chat_feature.presentation.screens.new_message.NewMessageScreen
import com.example.itami_chat.chat_feature.presentation.screens.new_message.NewMessageViewModel
import com.example.itami_chat.core.presentation.navigation.Graph
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.presentation.navigation.navigateForResult
import com.example.itami_chat.core.presentation.navigation.popBackStackWithResult


fun NavGraphBuilder.chatsGraph(
    navController: NavController,
    imageLoader: ImageLoader,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        route = Graph.CHATS_GRAPH,
        startDestination = Screen.Chats.fullRoute
    ) {
        composable(Screen.Chats.fullRoute) {
            val viewModel: ChatsViewModel = hiltViewModel()
            ChatsScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateToRoute = { route ->
                    navController.navigate(route) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToGroupChat = { chat ->
                    navController.navigate(Screen.GroupChat.getRouteWithArgs(chat)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToDialogChat = { user ->
                    navController.navigate(Screen.DialogChat.getRouteWithArgs(user)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToSearchUsers = {
                    navController.navigate(Screen.SearchUsers.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToNewMessage = {
                    navController.navigate(Screen.NewMessage.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToCreateNewGroup = {
                    navController.navigate(Screen.NewGroupParticipants.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToSettings = { },
                imageLoader = imageLoader,
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(Screen.NewMessage.fullRoute) {
            val viewModel: NewMessageViewModel = hiltViewModel()
            NewMessageScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateToSearchUsers = { },
                onNavigateToNewGroup = {
                    navController.navigate(Screen.NewGroupParticipants.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToDialogChat = { user ->
                    navController.navigate(Screen.DialogChat.getRouteWithArgs(user)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                searchQueryState = viewModel.searchQueryState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(route = Screen.NewGroupParticipants.fullRoute) {
            val viewModel: NewGroupParticipantsViewModel = hiltViewModel()
            NewGroupParticipantsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToNewGroupDetails = { userIds ->
                    navController.navigate(Screen.NewGroupDetails.getRouteWithArgs(userIds)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onShowSnackbar = onShowSnackbar,
                imageLoader = imageLoader,
                state = viewModel.state,
                searchQueryState = viewModel.searchQueryState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.NewGroupDetails.fullRoute,
        ) {
            val viewModel: NewGroupDetailsViewModel = hiltViewModel()
            NewGroupDetailsScreen(
                imageLoader = imageLoader,
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateBackToChats = {
                    navController.popBackStack(route = Screen.Chats.fullRoute, inclusive = false)
                },
                state = viewModel.state,
                chatNameState = viewModel.chatNameState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.DialogChat.fullRoute,
            arguments = listOf(
                navArgument(Screen.USER_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val viewModel: DialogChatViewModel = hiltViewModel()
            val userId = backStackEntry.arguments?.getInt(Screen.USER_ID_ARG)
                ?: throw RuntimeException("Argument userId was not passed")
            DialogChatScreen(
                dialogUserId = userId,
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Screen.UserProfile.getRouteWithArgs(userId)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                messageInputState = viewModel.messageInputState,
                editMessageInputState = viewModel.editMessageInputState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.GroupChat.fullRoute,
            arguments = listOf(
                navArgument(Screen.CHAT_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel: GroupChatViewModel = hiltViewModel()
            GroupChatScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToChatProfile = { chatId ->
                    navController.navigate(Screen.ChatProfile.getRouteWithArgs(chatId)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Screen.UserProfile.getRouteWithArgs(userId)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                messageInputState = viewModel.messageInputState,
                editMessageInputState = viewModel.editMessageInputState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.ChatProfile.fullRoute,
            arguments = listOf(
                navArgument(Screen.CHAT_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel: ChatProfileViewModel = hiltViewModel()
            ChatProfileScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateBackToChats = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack(
                            Screen.Chats.fullRoute,
                            false
                        )
                    }
                },
                onNavigateToEditChat = { chatId, callback ->
                    navController.navigateForResult(
                        Screen.EditChat.getRouteWithArgs(chatId),
                        navResultCallback = callback
                    )
                },
                onNavigateToAddChatParticipants = { chatId, callback ->
                    navController.navigateForResult(
                        Screen.AddChatParticipants.getRouteWithArgs(chatId),
                        navResultCallback = callback
                    )
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Screen.UserProfile.getRouteWithArgs(userId)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.EditChat.fullRoute,
            arguments = listOf(
                navArgument(Screen.CHAT_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel: EditChatViewModel = hiltViewModel()
            EditChatScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = { chat ->
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStackWithResult(chat)
                    }
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Screen.UserProfile.getRouteWithArgs(userId)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                chatNameState = viewModel.chatNameState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.AddChatParticipants.fullRoute,
            arguments = listOf(
                navArgument(Screen.CHAT_ID_ARG) {
                    type = NavType.IntType
                }
            ),
        ) {
            val viewModel: AddChatParticipantsViewModel = hiltViewModel()
            AddChatParticipantsScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = { participants ->
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStackWithResult(participants)
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                searchQueryState = viewModel.searchQueryState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
    }
}