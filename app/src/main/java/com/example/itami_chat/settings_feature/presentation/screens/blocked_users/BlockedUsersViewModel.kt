package com.example.itami_chat.settings_feature.presentation.screens.blocked_users

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.use_case.UnblockUserUseCase
import com.example.itami_chat.settings_feature.domain.use_case.GetBlockedUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockedUsersViewModel @Inject constructor(
    private val getBlockedUsersUseCase: GetBlockedUsersUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<BlockedUsersUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(BlockedUsersState())
        private set

    init {
        getBlockedUsers()
    }

    fun onEvent(event: BlockedUsersEvent) {
        when(event) {
            is BlockedUsersEvent.OnReloadBlockedUsers -> {
                getBlockedUsers()
            }
            is BlockedUsersEvent.OnUnblockUser -> {
                unblockUser(event.id)
            }
        }
    }

    private fun unblockUser(id: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = unblockUserUseCase(id)) {
                is AppResponse.Success -> {
                    val blockedUsers = state.blockedUsers.toMutableList().apply {
                        removeIf { it.id == id }
                    }
                    state = state.copy(blockedUsers = blockedUsers)
                    sendUiEvent(BlockedUsersUiEvent.OnShowSnackbar(
                        application.getString(R.string.text_user_unblocked))
                    )
                }

                is AppResponse.Failed -> {
                    handleException(exception = result.exception, message = result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun getBlockedUsers() {
        state = state.copy(isLoading = true, loadingUsersError = null)
        viewModelScope.launch {
            when (val result = getBlockedUsersUseCase()) {
                is AppResponse.Success -> {
                    state = state.copy(blockedUsers = result.data)
                }

                is AppResponse.Failed -> {
                    state = state.copy(
                        loadingUsersError = application.getString(R.string.text_failed_to_load_blocked_users)
                    )
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun sendUiEvent(event: BlockedUsersUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    BlockedUsersUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    BlockedUsersUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }


        }
    }
}