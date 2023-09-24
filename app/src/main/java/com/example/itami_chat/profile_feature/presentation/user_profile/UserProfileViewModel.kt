package com.example.itami_chat.profile_feature.presentation.user_profile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.use_case.AcceptContactRequestUseCase
import com.example.itami_chat.core.domain.use_case.BlockUserUseCase
import com.example.itami_chat.core.domain.use_case.CancelContactRequestUseCase
import com.example.itami_chat.core.domain.use_case.DeclineContactRequestUseCase
import com.example.itami_chat.core.domain.use_case.DeleteContactUseCase
import com.example.itami_chat.core.domain.use_case.SendContactRequestUseCase
import com.example.itami_chat.core.domain.use_case.UnblockUserUseCase
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.utils.Constants
import com.example.itami_chat.profile_feature.domain.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val sendContactRequestUseCase: SendContactRequestUseCase,
    private val declineContactRequestUseCase: DeclineContactRequestUseCase,
    private val cancelContactRequestUseCase: CancelContactRequestUseCase,
    private val acceptContactRequestUseCase: AcceptContactRequestUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<UserProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(UserProfileState())
        private set

    private var userId: Int = Constants.UNKNOWN_ID

    init {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            savedStateHandle.get<Int>(Screen.USER_ID_ARG)?.let { userId ->
                this@UserProfileViewModel.userId = userId
                when (val result = getUserProfileUseCase(userId)) {
                    is AppResponse.Success -> {
                        state = state.copy(userProfile = result.data)
                    }

                    is AppResponse.Failed -> {
                        handleException(result.exception, result.message)
                    }
                }
                state = state.copy(isLoading = false)
            } ?: throw RuntimeException("Argument userId was not passed")
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.OnAcceptContactRequest -> {
                state.userProfile?.contactRequest?.id?.let { acceptContactRequest(it) }
            }

            is UserProfileEvent.OnCancelContactRequest -> {
                state.userProfile?.contactRequest?.id?.let { cancelContactRequest(it) }
            }

            is UserProfileEvent.OnDeleteContact -> {
                state.userProfile?.userId?.let { deleteContact(it) }
            }

            is UserProfileEvent.OnDeclineContactRequest -> {
                state.userProfile?.contactRequest?.id?.let { declineContactRequest(it) }
            }

            is UserProfileEvent.OnSendContactRequest -> {
                state.userProfile?.userId?.let { sendContactRequest(it) }
            }

            is UserProfileEvent.OnBlockUser -> {
                state.userProfile?.userId?.let { blockUser(it) }
            }

            is UserProfileEvent.OnUnblockUser -> {
                state.userProfile?.userId?.let { unblockUser(it) }
            }
        }
    }

    private fun sendContactRequest(userId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = sendContactRequestUseCase(userId)) {
                is AppResponse.Success -> {
                    state = state.copy(
                        userProfile = state.userProfile?.copy(
                            contactRequest = result.data
                        )
                    )
                    sendUiEvent(
                        UserProfileUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_contact_request_has_been_sent)
                        )
                    )
                }

                is AppResponse.Failed -> {
                    result.exception.printStackTrace()
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun acceptContactRequest(requestId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = acceptContactRequestUseCase(requestId)) {
                is AppResponse.Success -> {
                    state = state.copy(
                        userProfile = state.userProfile?.copy(
                            isContact = true,
                            contactRequest = null
                        )
                    )
                    sendUiEvent(
                        UserProfileUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_contact_request_accepted)
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun declineContactRequest(requestId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = declineContactRequestUseCase(requestId)) {
                is AppResponse.Success -> {
                    state = state.copy(
                        userProfile = state.userProfile?.copy(
                            contactRequest = null
                        )
                    )
                    sendUiEvent(
                        UserProfileUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_contact_request_declined)
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun cancelContactRequest(requestId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = cancelContactRequestUseCase(requestId)) {
                is AppResponse.Success -> {
                    state = state.copy(
                        userProfile = state.userProfile?.copy(
                            contactRequest = null
                        )
                    )
                    sendUiEvent(
                        UserProfileUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_contact_request_canceled)
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun deleteContact(userId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = deleteContactUseCase(userId)) {
                is AppResponse.Success -> {
                    state = state.copy(
                        userProfile = state.userProfile?.copy(
                            isContact = false
                        )
                    )
                    sendUiEvent(
                        UserProfileUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_contact_deleted)
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun blockUser(userId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = blockUserUseCase(userId)) {
                is AppResponse.Success -> {
                    state = state.copy(
                        userProfile = state.userProfile?.copy(
                            isBlockedByMe = true,
                        )
                    )
                    sendUiEvent(
                        UserProfileUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_user_blocked)
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun unblockUser(userId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = unblockUserUseCase(userId)) {
                is AppResponse.Success -> {
                    state = state.copy(
                        userProfile = state.userProfile?.copy(
                            isBlockedByMe = false,
                        )
                    )
                    sendUiEvent(
                        UserProfileUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_user_unblocked)
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun sendUiEvent(event: UserProfileUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    UserProfileUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    UserProfileUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }
}