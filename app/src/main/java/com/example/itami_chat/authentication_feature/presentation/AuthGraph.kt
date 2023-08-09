package com.example.itami_chat.authentication_feature.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.itami_chat.authentication_feature.presentation.create_profile.CreateProfileScreen
import com.example.itami_chat.authentication_feature.presentation.create_profile.CreateProfileViewModel
import com.example.itami_chat.authentication_feature.presentation.forgot_password.ForgotPasswordScreen
import com.example.itami_chat.authentication_feature.presentation.forgot_password.ForgotPasswordViewModel
import com.example.itami_chat.authentication_feature.presentation.login.LoginScreen
import com.example.itami_chat.authentication_feature.presentation.login.LoginViewModel
import com.example.itami_chat.authentication_feature.presentation.onboarding.OnboardingScreen
import com.example.itami_chat.authentication_feature.presentation.onboarding.OnboardingViewModel
import com.example.itami_chat.authentication_feature.presentation.password_reset.PasswordResetScreen
import com.example.itami_chat.authentication_feature.presentation.password_reset.PasswordResetViewModel
import com.example.itami_chat.authentication_feature.presentation.register.RegisterScreen
import com.example.itami_chat.authentication_feature.presentation.register.RegisterViewModel
import com.example.itami_chat.authentication_feature.presentation.splash.SplashScreen
import com.example.itami_chat.authentication_feature.presentation.splash.SplashViewModel
import com.example.itami_chat.authentication_feature.presentation.verify_email.VerifyEmailScreen
import com.example.itami_chat.authentication_feature.presentation.verify_email.VerifyEmailViewModel
import com.example.itami_chat.core.presentation.navigation.Graph
import com.example.itami_chat.core.presentation.navigation.Screen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        route = Graph.AUTH_GRAPH,
        startDestination = Screen.Splash.fullRoute,
    ) {
        composable(Screen.Splash.fullRoute) {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                inclusive = true
                            }
                        }
                    }
                },
                onNavigateToChats = {
                    navController.navigate(
                        Graph.CHATS_GRAPH,
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                    navController.graph.setStartDestination(Graph.CHATS_GRAPH)
                },
                onShowSnackbar = onShowSnackbar,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(Screen.Onboarding.fullRoute) {
            val viewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.fullRoute) {
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
                onEvent = viewModel::onEvent
            )
        }
        composable(Screen.Login.fullRoute) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                onNavigateToChats = {
                    navController.navigate(
                        Graph.CHATS_GRAPH,
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                    navController.graph.setStartDestination(Graph.CHATS_GRAPH)
                },
                onNavigateToVerifyEmail = { email ->
                    navController.navigate(Screen.VerifyEmail.getRouteWithArgs(email)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.fullRoute) {
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
                    navController.popBackStack()
                },
                state = viewModel.state,
                emailFieldState = viewModel.emailState,
                passwordFieldState = viewModel.passwordState,
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(Screen.Register.fullRoute) {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                onNavigateToVerifyEmail = { email ->
                    navController.navigate(Screen.VerifyEmail.getRouteWithArgs(email)) {
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
                    navController.popBackStack()
                },
                state = viewModel.state,
                emailFieldState = viewModel.emailState,
                passwordFieldState = viewModel.passwordState,
                confirmPasswordFieldState = viewModel.confirmPasswordState,
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(
            route = Screen.VerifyEmail.fullRoute,
            arguments = listOf(
                navArgument(Screen.EMAIL_ARG) {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val email = navBackStackEntry.arguments?.getString(Screen.EMAIL_ARG)
                ?: throw RuntimeException("Missing email argument.")
            val viewModel: VerifyEmailViewModel = hiltViewModel()

            VerifyEmailScreen(
                email = email,
                onNavigateToCreateProfile = {
                    navController.navigate(Screen.CreateProfile.fullRoute) {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onShowSnackbar = onShowSnackbar,
                state = viewModel.state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(route = Screen.CreateProfile.fullRoute) {
            val viewModel: CreateProfileViewModel = hiltViewModel()
            CreateProfileScreen(
                onNavigateToChats = {
                    navController.navigate(
                        Graph.CHATS_GRAPH,
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                    navController.graph.setStartDestination(Graph.CHATS_GRAPH)
                },
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.Onboarding.fullRoute,
                        inclusive = false
                    )
                },
                state = viewModel.state,
                fullNameState = viewModel.fullNameState,
                bioState = viewModel.bioState,
                imageUriState = viewModel.imageUriState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(route = Screen.ForgotPassword.fullRoute) {
            val viewModel: ForgotPasswordViewModel = hiltViewModel()
            ForgotPasswordScreen(
                onNavigateToPasswordReset = { email ->
                    navController.navigate(Screen.PasswordReset.getRouteWithArgs(email)) {
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
                    navController.popBackStack()
                },
                state = viewModel.state,
                emailState = viewModel.emailState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.PasswordReset.fullRoute,
            arguments = listOf(
                navArgument(Screen.EMAIL_ARG) {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val email = navBackStackEntry.arguments?.getString(Screen.EMAIL_ARG)
                ?: throw RuntimeException("Missing email argument.")
            val viewModel: PasswordResetViewModel = hiltViewModel()
            PasswordResetScreen(
                email = email,
                onNavigateToLogin = {
                    navController.popBackStack(route = Screen.Login.fullRoute, inclusive = false)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onShowSnackbar = onShowSnackbar,
                state = viewModel.state,
                codeState = viewModel.codeState,
                passwordState = viewModel.passwordState,
                confirmPasswordState = viewModel.confirmPasswordState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
    }
}