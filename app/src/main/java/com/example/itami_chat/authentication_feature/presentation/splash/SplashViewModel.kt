package com.example.itami_chat.authentication_feature.presentation.splash

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.authentication_feature.domain.use_case.AuthenticateUseCase
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.model.AppResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val application: Application,
    private val authenticateUseCase: AuthenticateUseCase,
) : ViewModel() {

    private val _uiEvent = Channel<SplashUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        authenticate()
    }


    private fun authenticate() {
        viewModelScope.launch {
            when (val response = authenticateUseCase()) {
                is AppResponse.Success -> {
                    _uiEvent.send(SplashUiEvent.Authenticated)
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }
            }
        }
    }


    private fun handleException(exception: Exception, exceptionMessage: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        SplashUiEvent.OnShowSnackbar(
                            application.getString(R.string.error_poor_network_connection)
                        )
                    )
                    _uiEvent.send(SplashUiEvent.NotAuthenticated)
                }
            }

            is UnauthorizedException -> {
                viewModelScope.launch {
                    _uiEvent.send(SplashUiEvent.NotAuthenticated)
                }
            }

            else -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        SplashUiEvent.OnShowSnackbar(
                            exceptionMessage ?: application.getString(R.string.error_unknown)
                        )
                    )
                    _uiEvent.send(SplashUiEvent.NotAuthenticated)
                }
            }
        }
    }

}