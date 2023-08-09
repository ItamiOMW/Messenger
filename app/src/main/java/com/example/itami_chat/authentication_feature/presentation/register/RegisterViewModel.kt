package com.example.itami_chat.authentication_feature.presentation.register

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.domain.use_case.RegisterUseCase
import com.example.itami_chat.core.domain.exception.EmptyConfirmPasswordFieldException
import com.example.itami_chat.core.domain.exception.EmptyEmailFieldException
import com.example.itami_chat.core.domain.exception.EmptyPasswordFieldException
import com.example.itami_chat.core.domain.exception.InvalidEmailException
import com.example.itami_chat.core.domain.exception.PasswordsDoNotMatchException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ShortPasswordException
import com.example.itami_chat.core.domain.exception.UserAlreadyExistsException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.presentation.state.PasswordTextFieldState
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val application: Application,
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    var emailState by mutableStateOf(StandardTextFieldState())
        private set

    var passwordState by mutableStateOf(PasswordTextFieldState())
        private set

    var confirmPasswordState by mutableStateOf(PasswordTextFieldState())
        private set

    private val _uiEvent = Channel<RegisterUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnEmailInputChange -> {
                emailState = emailState.copy(text = event.newValue, errorMessage = null)
            }

            is RegisterEvent.OnPasswordInputChange -> {
                passwordState = passwordState.copy(text = event.newValue, errorMessage = null)
                confirmPasswordState = confirmPasswordState.copy(errorMessage = null)
            }

            is RegisterEvent.OnConfirmPasswordInputChange -> {
                confirmPasswordState = confirmPasswordState.copy(
                    text = event.newValue, errorMessage = null
                )
            }

            is RegisterEvent.OnChangePasswordVisibility -> {
                passwordState = passwordState.copy(
                    isPasswordVisible = !passwordState.isPasswordVisible
                )
            }

            is RegisterEvent.OnChangeConfirmPasswordVisibility -> {
                confirmPasswordState = confirmPasswordState.copy(
                    isPasswordVisible = !confirmPasswordState.isPasswordVisible
                )
            }

            is RegisterEvent.OnRegister -> {
                register(emailState.text, passwordState.text, confirmPasswordState.text)
            }
        }
    }

    private fun register(email: String, password: String, confirmPassword: String) {
        state = state.copy(isRegistering = true, error = null)
        viewModelScope.launch {
            val registerResult = registerUseCase(email, password, confirmPassword)

            if (registerResult.emailError != null) {
                handleException(registerResult.emailError, message = null)
            }

            if (registerResult.passwordError != null) {
                handleException(registerResult.passwordError, message = null)
            }

            if (registerResult.confirmPasswordError != null) {
                handleException(registerResult.confirmPasswordError, message = null)
            }

            when (val response = registerResult.result) {
                is AppResponse.Success -> {
                    _uiEvent.send(RegisterUiEvent.OnNavigateToVerifyEmail(email))
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }

                null -> Unit
            }
            state = state.copy(isRegistering = false)
        }
    }


    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                state = state.copy(
                    error = application.getString(R.string.error_poor_network_connection)
                )
            }

            is UserAlreadyExistsException -> {
                emailState =
                    emailState.copy(errorMessage = application.getString(R.string.error_user_already_exists))
            }

            is InvalidEmailException -> {
                emailState = emailState.copy(
                    errorMessage = application.getString(R.string.error_invalid_email)
                )
            }

            is ShortPasswordException -> {
                passwordState = passwordState.copy(
                    errorMessage = application.getString(R.string.error_short_password)
                )
            }

            is EmptyEmailFieldException -> {
                emailState = emailState.copy(
                    errorMessage = application.getString(R.string.error_empty_field)
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

            is PasswordsDoNotMatchException -> {
                confirmPasswordState = confirmPasswordState.copy(
                    errorMessage = application.getString(R.string.error_passwords_do_not_match)
                )
            }


            else -> {
                state = state.copy(
                    error = message ?: application.getString(R.string.error_unknown)
                )
            }
        }
    }
}