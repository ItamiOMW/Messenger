package com.example.itami_chat.authentication_feature.presentation.password_reset

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.domain.use_case.ResetPasswordUseCase
import com.example.itami_chat.core.domain.exception.EmptyConfirmPasswordFieldException
import com.example.itami_chat.core.domain.exception.EmptyPasswordFieldException
import com.example.itami_chat.core.domain.exception.InvalidVerificationCodeException
import com.example.itami_chat.core.domain.exception.PasswordsDoNotMatchException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ShortPasswordException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.presentation.state.PasswordTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val application: Application,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state by mutableStateOf(PasswordResetState())
        private set

    var passwordState by mutableStateOf(PasswordTextFieldState())
        private set

    var confirmPasswordState by mutableStateOf(PasswordTextFieldState())
        private set

    var codeState by mutableStateOf("")
        private set

    private val _uiEvent = Channel<PasswordResetUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private lateinit var email: String

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>(Screen.EMAIL_ARG)?.let { email ->
                this@PasswordResetViewModel.email = email
            } ?: throw RuntimeException("Missing email argument.")
        }
    }


    fun onEvent(event: PasswordResetEvent) {
        when (event) {
            is PasswordResetEvent.OnPasswordInputChange -> {
                passwordState = passwordState.copy(text = event.newValue, errorMessage = null)
            }

            is PasswordResetEvent.OnConfirmPasswordInputChange -> {
                confirmPasswordState = confirmPasswordState.copy(
                    text = event.newValue,
                    errorMessage = null
                )
            }

            is PasswordResetEvent.OnCodeInputChange -> {
                codeState = event.newValue
            }

            is PasswordResetEvent.OnChangeConfirmPasswordVisibility -> {
                passwordState = passwordState.copy(
                    isPasswordVisible = !passwordState.isPasswordVisible
                )
            }

            is PasswordResetEvent.OnChangePasswordVisibility -> {
                confirmPasswordState = confirmPasswordState.copy(
                    isPasswordVisible = !confirmPasswordState.isPasswordVisible
                )
            }

            is PasswordResetEvent.OnConfirm -> {
                resetPassword(
                    email = email,
                    code = codeState.toInt(),
                    newPassword = passwordState.text,
                    confirmNewPassword = confirmPasswordState.text
                )
            }
        }
    }


    private fun resetPassword(
        email: String,
        code: Int,
        newPassword: String,
        confirmNewPassword: String,
    ) {
        state = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val resetPasswordResult = resetPasswordUseCase(
                email, code, newPassword, confirmNewPassword
            )
            if (resetPasswordResult.codeError != null) {
                handleException(resetPasswordResult.codeError, null)
            }
            if (resetPasswordResult.passwordError != null) {
                handleException(resetPasswordResult.passwordError, null)
            }
            if (resetPasswordResult.confirmPasswordError != null) {
                handleException(resetPasswordResult.confirmPasswordError, null)
            }
            when (val response = resetPasswordResult.result) {
                is AppResponse.Success -> {
                    _uiEvent.send(
                        PasswordResetUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_successfully_reset_the_password)
                        )
                    )
                    _uiEvent.send(PasswordResetUiEvent.OnPasswordResetSuccessful)
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }

                null -> Unit
            }
            state = state.copy(isLoading = false)
        }
    }


    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                state = state.copy(
                    errorMessage = application.getString(R.string.error_poor_network_connection)
                )
            }

            is InvalidVerificationCodeException -> {
                state = state.copy(
                    errorMessage = application.getString(R.string.error_invalid_verification_code)
                )
            }

            is ShortPasswordException -> {
                state = state.copy(
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
                state = state.copy(
                    errorMessage = message ?: application.getString(R.string.error_unknown)
                )
            }
        }
    }

}