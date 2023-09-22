package com.example.itami_chat.profile_feature.presentation


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import coil.ImageLoader
import com.example.itami_chat.core.presentation.navigation.Graph
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.profile_feature.presentation.edit_profile.EditProfileScreen
import com.example.itami_chat.profile_feature.presentation.edit_profile.EditProfileViewModel
import com.example.itami_chat.profile_feature.presentation.search_users.SearchUsersScreen
import com.example.itami_chat.profile_feature.presentation.search_users.SearchUsersViewModel
import com.example.itami_chat.profile_feature.presentation.user_profile.UserProfileScreen
import com.example.itami_chat.profile_feature.presentation.user_profile.UserProfileViewModel


fun NavGraphBuilder.profileGraph(
    navController: NavController,
    imageLoader: ImageLoader,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        route = Graph.PROFILE_GRAPH,
        startDestination = Screen.UserProfile.fullRoute
    ) {
        composable(
            route = Screen.UserProfile.fullRoute,
            arguments = listOf(
                navArgument(Screen.USER_ID_ARG) {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            }
        ) {
            val viewModel: UserProfileViewModel = hiltViewModel()
            UserProfileScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToDialogChat = { userId ->
                    navController.navigate(Screen.DialogChat.getRouteWithArgs(userId)) {
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
            Screen.EditProfile.fullRoute,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            }
        ) {
            val viewModel: EditProfileViewModel = hiltViewModel()
            EditProfileScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                pictureUriState = viewModel.profilePictureUriState,
                fullNameInputState = viewModel.fullNameInputState,
                usernameInputState = viewModel.usernameInputState,
                bioInputState = viewModel.bioInputState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            Screen.SearchUsers.fullRoute,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            }
        ) {
            val viewModel: SearchUsersViewModel = hiltViewModel()
            SearchUsersScreen(
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
                searchInputState = viewModel.searchInputState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
    }
}