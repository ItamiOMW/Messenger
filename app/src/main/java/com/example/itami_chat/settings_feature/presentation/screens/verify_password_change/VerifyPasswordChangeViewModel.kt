package com.example.itami_chat.settings_feature.presentation.screens.verify_password_change

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.settings_feature.domain.use_case.SendPasswordChangeCodeUseCase
import com.example.itami_chat.settings_feature.domain.use_case.VerifyPasswordChangeCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyPasswordChangeViewModel @Inject constructor(
    private val verifyPasswordChangeCodeUseCase: VerifyPasswordChangeCodeUseCase,
    private val sendPasswordChangeCodeUseCase: SendPasswordChangeCodeUseCase,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<VerifyPasswordChangeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(VerifyPasswordChangeState())
        private set

    var codeState by mutableStateOf("")
        private set

    init {
        getMyUser()
    }

    fun onEvent(event: VerifyPasswordChangeEvent) {
        when (event) {
            is VerifyPasswordChangeEvent.OnCodeValueChange -> {
                codeState = event.newValue
            }

            is VerifyPasswordChangeEvent.OnVerifyCodeChange -> {
                verifyCodeChange(codeState.toInt())
            }

            is VerifyPasswordChangeEvent.OnResendCode -> {
                sendPasswordChangeCode()
            }
        }
    }

    private fun sendPasswordChangeCode() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = sendPasswordChangeCodeUseCase()) {
                is AppResponse.Success -> {
                    sendUiEvent(VerifyPasswordChangeUiEvent.OnShowSnackbar(application.getString(R.string.text_password_change_code_sent)))
                }

                is AppResponse.Failed -> {
                    handleException(exception = result.exception, message = result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun verifyCodeChange(code: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = verifyPasswordChangeCodeUseCase(code)) {
                is AppResponse.Success -> {
                    sendUiEvent(VerifyPasswordChangeUiEvent.OnCodeVerified)
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                state = state.copy(myUser = myUser)
            }
        }
    }

    private fun sendUiEvent(event: VerifyPasswordChangeUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    VerifyPasswordChangeUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    VerifyPasswordChangeUiEvent.OnShowSnackbar(
                        message ?: application.getString(R.string.error_unknown)
                    )
                )
            }
        }
    }

}