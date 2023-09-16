package com.example.itami_chat.authentication_feature.presentation.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.domain.use_case.LoginUseCase
import com.example.itami_chat.core.domain.exception.EmptyEmailFieldException
import com.example.itami_chat.core.domain.exception.EmptyPasswordFieldException
import com.example.itami_chat.core.domain.exception.InvalidEmailException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.exception.UserDoesNotExistException
import com.example.itami_chat.core.domain.exception.UserNotActiveException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.presentation.state.PasswordTextFieldState
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val application: Application,
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    var emailState by mutableStateOf(StandardTextFieldState())
        private set

    var isDarkMode by mutableStateOf(false)
        private set

    var passwordState by mutableStateOf(PasswordTextFieldState())
        private set

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailInputChange -> {
                emailState = emailState.copy(text = event.newValue, errorMessage = null)
            }

            is LoginEvent.OnPasswordInputChange -> {
                passwordState = passwordState.copy(text = event.newValue, errorMessage = null)
            }

            is LoginEvent.OnChangePasswordVisibility -> {
                passwordState = passwordState.copy(
                    isPasswordVisible = !passwordState.isPasswordVisible
                )
            }

            is LoginEvent.OnLogin -> {
                login(emailState.text, passwordState.text)
            }
        }
    }

    private fun login(email: String, password: String) {
        state = state.copy(isLoggingIn = true, error = null)
        viewModelScope.launch {
            val loginResult = loginUseCase(email, password)

            if (loginResult.emailError != null) {
                handleException(loginResult.emailError, null)
            }

            if (loginResult.passwordError != null) {
                handleException(loginResult.passwordError, null)
            }

            when (val response = loginResult.result) {
                is AppResponse.Success -> {
                    _uiEvent.send(LoginUiEvent.OnNavigateToChats)
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }

                null -> Unit
            }
            state = state.copy(isLoggingIn = false)
        }
    }


    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is UserNotActiveException -> {
                viewModelScope.launch {
                    _uiEvent.send(LoginUiEvent.OnNavigateToVerifyEmail(emailState.text))
                }
            }

            is PoorNetworkConnectionException -> {
                state = state.copy(
                    error = application.getString(R.string.error_poor_network_connection)
                )
            }

            is UnauthorizedException -> {
                state = state.copy(
                    error = application.getString(R.string.error_invalid_email_or_password)
                )
            }

            is UserDoesNotExistException -> {
                state = state.copy(
                    error = application.getString(R.string.error_invalid_email_or_password)
                )
            }

            is InvalidEmailException -> {
                emailState = emailState.copy(
                    errorMessage = application.getString(R.string.error_invalid_email)
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

            else -> {
                state = state.copy(
                    error = message ?: application.getString(R.string.error_unknown)
                )
            }
        }
    }
}