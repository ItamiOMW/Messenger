package com.example.itami_chat.authentication_feature.presentation.forgot_password

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.domain.use_case.SendPasswordResetCodeUseCase
import com.example.itami_chat.core.domain.exception.InvalidEmailException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val application: Application,
    private val sendPasswordResetCodeUseCase: SendPasswordResetCodeUseCase,
) : ViewModel() {

    var state by mutableStateOf(ForgotPasswordState())
        private set

    var emailState by mutableStateOf(StandardTextFieldState())
        private set

    private var _uiEvent = Channel<ForgotPasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.OnConfirm -> {
                sendPasswordResetCode(emailState.text)
            }

            is ForgotPasswordEvent.OnEmailInputChange -> {
                emailState = emailState.copy(text = event.newValue, errorMessage = null)
            }
        }
    }


    private fun sendPasswordResetCode(email: String) {
        state = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val response = sendPasswordResetCodeUseCase(email)) {
                is AppResponse.Success -> {
                    _uiEvent.send(ForgotPasswordUiEvent.OnPasswordResetCodeSent(email))
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }
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

            is InvalidEmailException -> {
                state = state.copy(
                    errorMessage = application.getString(R.string.error_invalid_email)
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