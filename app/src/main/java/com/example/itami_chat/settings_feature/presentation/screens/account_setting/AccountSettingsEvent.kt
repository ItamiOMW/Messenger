package com.example.itami_chat.settings_feature.presentation.screens.account_setting

sealed class AccountSettingsEvent {

    data object OnSendPasswordChangeCode: AccountSettingsEvent()

    data object OnDeleteAccount: AccountSettingsEvent()

    data object OnLogout: AccountSettingsEvent()

    data object OnShowDeleteAccountDialog: AccountSettingsEvent()

    data object OnHideDeleteAccountDialog: AccountSettingsEvent()

}
