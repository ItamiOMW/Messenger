package com.example.itami_chat.settings_feature.presentation.screens.account_setting

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.use_case.LogoutUseCase
import com.example.itami_chat.settings_feature.domain.use_case.DeleteAccountUseCase
import com.example.itami_chat.settings_feature.domain.use_case.SendPasswordChangeCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel @Inject constructor(
    private val sendPasswordChangeCodeUseCase: SendPasswordChangeCodeUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<AccountSettingsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(AccountSettingState())
        private set

    fun onEvent(event: AccountSettingsEvent) {
        when (event) {
            is AccountSettingsEvent.OnSendPasswordChangeCode -> {
                sendPasswordChangeCode()
            }

            is AccountSettingsEvent.OnDeleteAccount -> {
                deleteAccount()
            }

            is AccountSettingsEvent.OnLogout -> {
                logout()
            }

            is AccountSettingsEvent.OnHideDeleteAccountDialog -> {
                state = state.copy(showDeleteAccountDialog = false)
            }

            is AccountSettingsEvent.OnShowDeleteAccountDialog -> {
                state = state.copy(showDeleteAccountDialog = true)
            }
        }
    }

    private fun deleteAccount() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = deleteAccountUseCase()) {
                is AppResponse.Success -> {
                    sendUiEvent(
                        AccountSettingsUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_account_was_deleted)
                        )
                    )
                    sendUiEvent(AccountSettingsUiEvent.OnAccountDeleted)
                }

                is AppResponse.Failed -> {
                    handleException(exception = result.exception, message = result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiEvent.send(AccountSettingsUiEvent.OnLogout)
        }
    }

    private fun sendPasswordChangeCode() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = sendPasswordChangeCodeUseCase()) {
                is AppResponse.Success -> {
                    sendUiEvent(AccountSettingsUiEvent.OnChangePasswordCodeSent)
                }

                is AppResponse.Failed -> {
                    handleException(exception = result.exception, message = result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun sendUiEvent(event: AccountSettingsUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    AccountSettingsUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    AccountSettingsUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}