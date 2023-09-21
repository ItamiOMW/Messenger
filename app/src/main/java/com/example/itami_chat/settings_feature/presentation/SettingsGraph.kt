package com.example.itami_chat.settings_feature.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import coil.ImageLoader
import com.example.itami_chat.core.presentation.navigation.Graph
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.settings_feature.presentation.screens.account_setting.AccountSettingScreen
import com.example.itami_chat.settings_feature.presentation.screens.account_setting.AccountSettingViewModel
import com.example.itami_chat.settings_feature.presentation.screens.appearance_setting.AppearanceSettingScreen
import com.example.itami_chat.settings_feature.presentation.screens.appearance_setting.AppearanceSettingViewModel
import com.example.itami_chat.settings_feature.presentation.screens.blocked_users.BlockedUsersScreen
import com.example.itami_chat.settings_feature.presentation.screens.blocked_users.BlockedUsersViewModel
import com.example.itami_chat.settings_feature.presentation.screens.change_password.ChangePasswordScreen
import com.example.itami_chat.settings_feature.presentation.screens.change_password.ChangePasswordViewModel
import com.example.itami_chat.settings_feature.presentation.screens.messages_permission.MessagesPermissionScreen
import com.example.itami_chat.settings_feature.presentation.screens.messages_permission.MessagesPermissionViewModel
import com.example.itami_chat.settings_feature.presentation.screens.privacy_setting.PrivacySettingScreen
import com.example.itami_chat.settings_feature.presentation.screens.privacy_setting.PrivacySettingViewModel
import com.example.itami_chat.settings_feature.presentation.screens.settings.SettingsScreen
import com.example.itami_chat.settings_feature.presentation.screens.settings.SettingsViewModel
import com.example.itami_chat.settings_feature.presentation.screens.verify_password_change.VerifyPasswordChangeScreen
import com.example.itami_chat.settings_feature.presentation.screens.verify_password_change.VerifyPasswordChangeViewModel


fun NavGraphBuilder.settingsGraph(
    navController: NavController,
    imageLoader: ImageLoader,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        route = Graph.SETTINGS_GRAPH,
        startDestination = Screen.Settings.fullRoute
    ) {
        composable(Screen.Settings.fullRoute) {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToEditProfile = {
                    navController.navigate(Screen.EditProfile.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(
                        Graph.AUTH_GRAPH
                    ) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToAccountSetting = {
                    navController.navigate(Screen.AccountSetting.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToPrivacySetting = {
                    navController.navigate(Screen.PrivacySetting.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToAppearanceSetting = {
                    navController.navigate(Screen.AppearanceSetting.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToNotificationsSetting = {
                    onShowSnackbar("Not implemented yet")
                },
                onNavigateToLanguageSetting = {
                    onShowSnackbar("Not implemented yet")
                },
                onNavigateToAboutApplication = {
                    onShowSnackbar("Not implemented yet")
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.AccountSetting.fullRoute) {
            val viewModel: AccountSettingViewModel = hiltViewModel()
            AccountSettingScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToVerifyPasswordChange = {
                    navController.navigate(Screen.VerifyPasswordChange.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(
                        Graph.AUTH_GRAPH
                    ) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                state = viewModel.state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.VerifyPasswordChange.fullRoute) {
            val viewModel: VerifyPasswordChangeViewModel = hiltViewModel()
            VerifyPasswordChangeScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToChangePassword = {
                    navController.navigate(Screen.ChangePassword.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                state = viewModel.state,
                codeState = viewModel.codeState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.ChangePassword.fullRoute) {
            val viewModel: ChangePasswordViewModel = hiltViewModel()
            ChangePasswordScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack(
                            route = Screen.VerifyPasswordChange.fullRoute,
                            inclusive = true
                        )
                    }
                },
                state = viewModel.state,
                passwordState = viewModel.passwordState,
                confirmPasswordState = viewModel.confirmPasswordState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.PrivacySetting.fullRoute) {
            val viewModel: PrivacySettingViewModel = hiltViewModel()
            PrivacySettingScreen(
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToBlockedUsers = {
                    navController.navigate(Screen.BlockedUsers.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToMessagesPermission = {
                    navController.navigate(Screen.MessagesPermission.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                state = viewModel.state,
            )
        }
        composable(Screen.BlockedUsers.fullRoute) {
            val viewModel: BlockedUsersViewModel = hiltViewModel()
            BlockedUsersScreen(
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
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.MessagesPermission.fullRoute) {
            val viewModel: MessagesPermissionViewModel = hiltViewModel()
            MessagesPermissionScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                state = viewModel.state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.AppearanceSetting.fullRoute) {
            val viewModel: AppearanceSettingViewModel = hiltViewModel()
            AppearanceSettingScreen(
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                state = viewModel.state,
                onEvent = viewModel::onEvent
            )
        }
    }
}