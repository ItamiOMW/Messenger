package com.example.itami_chat.authentication_feature.presentation.verify_email

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.domain.use_case.ResendEmailVerificationCodeUseCase
import com.example.itami_chat.authentication_feature.domain.use_case.VerifyEmailUseCase
import com.example.itami_chat.core.domain.exception.InvalidVerificationCodeException
import com.example.itami_chat.core.domain.exception.UserDoesNotExistException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resendEmailVerificationCodeUseCase: ResendEmailVerificationCodeUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<VerifyEmailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(VerifyEmailState())
        private set

    private lateinit var email: String


    init {
        viewModelScope.launch {
            savedStateHandle.get<String>(Screen.EMAIL_ARG)?.let { email ->
                this@VerifyEmailViewModel.email = email
            } ?: throw RuntimeException("Email argument wasn't passed")
        }
    }


    fun onEvent(event: VerifyEmailEvent) {
        when (event) {
            is VerifyEmailEvent.OnCodeInputChange -> {
                state = state.copy(codeValue = event.newValue, error = null)
            }

            is VerifyEmailEvent.OnConfirm -> {
                state = state.copy(error = null)
                verifyEmail(email, state.codeValue.toInt())
            }

            is VerifyEmailEvent.OnResendCode -> {
                resendCode(email)
            }
        }
    }

    private fun verifyEmail(email: String, code: Int) {
        state = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val response = verifyEmailUseCase(email, code)) {
                is AppResponse.Success -> {
                    _uiEvent.send(VerifyEmailUiEvent.OnEmailVerified)
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }


    private fun resendCode(email: String) {
        state = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val response = resendEmailVerificationCodeUseCase(email)) {
                is AppResponse.Success -> {
                    _uiEvent.send(
                        VerifyEmailUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_email_verification_code_sent)
                        )
                    )
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
            is InvalidVerificationCodeException -> {
                state = state.copy(
                    error = application.getString(R.string.error_invalid_verification_code)
                )
            }

            is UserDoesNotExistException -> {
                state = state.copy(
                    error = application.getString(R.string.error_user_does_not_exist)
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