package com.example.itami_chat.settings_feature.presentation.screens.messages_permission

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MessagesPermission
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.settings_feature.domain.use_case.ChangeMessagesPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesPermissionViewModel @Inject constructor(
    private val changeMessagesPermissionUseCase: ChangeMessagesPermissionUseCase,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<MessagesPermissionUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(MessagesPermissionState())
        private set

    init {
        getMyUser()
    }

    fun onEvent(event: MessagesPermissionEvent) {
        when(event) {
            is MessagesPermissionEvent.OnSave -> {
                changeMessagesPermission(state.selectedPermission)
            }
            is MessagesPermissionEvent.OnSelectPermission -> {
                state = state.copy(selectedPermission = event.permission)
            }
        }
    }

    private fun changeMessagesPermission(permission: MessagesPermission) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = changeMessagesPermissionUseCase(permission)) {
                is AppResponse.Success -> {
                    sendUiEvent(
                        MessagesPermissionUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_messages_permission_changed)
                        )
                    )
                    sendUiEvent(MessagesPermissionUiEvent.OnNavigateBack)
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                state = state.copy(myUser = myUser, selectedPermission = myUser.messagesPermission)
            }
        }
    }

    private fun sendUiEvent(event: MessagesPermissionUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    MessagesPermissionUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    MessagesPermissionUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}