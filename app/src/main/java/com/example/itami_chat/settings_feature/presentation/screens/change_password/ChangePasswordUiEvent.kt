package com.example.itami_chat.settings_feature.presentation.screens.change_password

sealed class ChangePasswordUiEvent {

    data class OnShowSnackbar(val message: String): ChangePasswordUiEvent()

    data object OnNavigateBack: ChangePasswordUiEvent()

}
