package com.example.itami_chat.settings_feature.presentation.screens.change_password

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.EmptyConfirmPasswordFieldException
import com.example.itami_chat.core.domain.exception.EmptyPasswordFieldException
import com.example.itami_chat.core.domain.exception.PasswordsDoNotMatchException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ShortPasswordException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.presentation.state.PasswordTextFieldState
import com.example.itami_chat.settings_feature.domain.use_case.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<ChangePasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(ChangePasswordState())
        private set

    var passwordState by mutableStateOf(PasswordTextFieldState())
        private set

    var confirmPasswordState by mutableStateOf(PasswordTextFieldState())
        private set

    fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.OnConfirmPasswordInputChange -> {
                confirmPasswordState = confirmPasswordState.copy(
                    text = event.newValue,
                    errorMessage = null
                )
            }
            is ChangePasswordEvent.OnPasswordInputChange -> {
                passwordState = passwordState.copy(
                    text = event.newValue,
                    errorMessage = null
                )
            }
            is ChangePasswordEvent.OnChangeConfirmPasswordVisibility -> {
                confirmPasswordState = confirmPasswordState.copy(isPasswordVisible = !confirmPasswordState.isPasswordVisible)
            }
            is ChangePasswordEvent.OnChangePasswordVisibility -> {
                passwordState = passwordState.copy(isPasswordVisible = !passwordState.isPasswordVisible)
            }
            is ChangePasswordEvent.OnChangePassword -> {
                changePassword(
                    passwordState.text,
                    confirmPasswordState.text
                )
            }
        }
    }

    private fun changePassword(
        password: String,
        confirmPassword: String
    ) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val response = changePasswordUseCase(password, confirmPassword)
            if (response.passwordError != null) {
                handleException(response.passwordError, null)
            }
            if (response.confirmPasswordError != null) {
                handleException(response.confirmPasswordError, null)
            }
            when(val result = response.result) {
                is AppResponse.Success -> {
                    sendUiEvent(ChangePasswordUiEvent.OnShowSnackbar(application.getString(R.string.text_password_changed)))
                    sendUiEvent(ChangePasswordUiEvent.OnNavigateBack)
                }
                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
                null -> Unit
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun sendUiEvent(event: ChangePasswordUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    ChangePasswordUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            is ShortPasswordException -> {
                passwordState = passwordState.copy(
                    errorMessage = application.getString(R.string.error_short_password)
                )
            }

            is PasswordsDoNotMatchException -> {
                confirmPasswordState = confirmPasswordState.copy(
                    errorMessage = application.getString(R.string.error_passwords_do_not_match)
                )
            }

            is EmptyPasswordFieldException -> {
                passwordState = passwordState.copy(
                    errorMessage = application.getString(R.string.error_empty_field)
                )
            }

            is EmptyConfirmPasswordFieldException -> {
                confirmPasswordState = confirmPasswordState.copy(
                    errorMessage = application.getString(R.string.error_empty_field)
                )
            }

            else -> {
                sendUiEvent(
                    ChangePasswordUiEvent.OnShowSnackbar(
                        message ?: application.getString(R.string.error_unknown)
                    )
                )
            }
        }
    }
}