package com.example.itami_chat.authentication_feature.presentation.splash

sealed class SplashUiEvent {

    data object Authenticated: SplashUiEvent()

    data object NotAuthenticated: SplashUiEvent()

    data class OnShowSnackbar(val message: String): SplashUiEvent()

}
