package com.example.itami_chat.settings_feature.presentation.screens.account_setting

sealed class AccountSettingsUiEvent {

    data object OnChangePasswordCodeSent: AccountSettingsUiEvent()

    data object OnLogout: AccountSettingsUiEvent()

    data object OnAccountDeleted: AccountSettingsUiEvent()

    data class OnShowSnackbar(val message: String): AccountSettingsUiEvent()

}
