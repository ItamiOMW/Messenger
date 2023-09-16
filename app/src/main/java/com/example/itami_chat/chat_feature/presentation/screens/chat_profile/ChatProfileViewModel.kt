package com.example.itami_chat.chat_feature.presentation.screens.chat_profile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.use_case.GetChatByIdUseCase
import com.example.itami_chat.chat_feature.domain.use_case.LeaveChatUseCase
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatProfileViewModel @Inject constructor(
    private val getChatByIdUseCase: GetChatByIdUseCase,
    private val leaveChatUseCase: LeaveChatUseCase,
    private val userManager: UserManager,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<ChatProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(ChatProfileState())
        private set

    private var chatId: Int = Constants.UNKNOWN_ID

    private lateinit var myUser: MyUser

    init {
        state = state.copy(isLoading = true)
        getMyUser()
        viewModelScope.launch {
            savedStateHandle.get<Int>(Screen.CHAT_ID_ARG)?.let { id ->
                when (val result = getChatByIdUseCase(id)) {
                    is AppResponse.Success -> {
                        chatId = result.data.id
                        val me = result.data.participants.find { it.user.id == myUser.id }
                        if (me == null) {
                            sendUiEvent(
                                ChatProfileUiEvent.OnShowSnackbar(
                                    application.getString(R.string.error_not_chat_participant)
                                )
                            )
                            sendUiEvent(ChatProfileUiEvent.OnNavigateBack)
                            return@launch
                        }
                        state = state.copy(chat = result.data, me = me)
                    }

                    is AppResponse.Failed -> {
                        handleException(exception = result.exception, message = result.message)
                    }
                }
                state = state.copy(isLoading = false)
            } ?: throw RuntimeException("Argument chatId was not passed")
        }
    }

    fun onEvent(event: ChatProfileEvent) {
        when (event) {
            is ChatProfileEvent.OnLeaveChat -> {
                state = state.copy(showLeaveChatDialog = false)
                leaveChat(chatId)
            }

            is ChatProfileEvent.OnHideLeaveChatDialog -> {
                state = state.copy(showLeaveChatDialog = false)
            }

            is ChatProfileEvent.OnShowLeaveChatDialog -> {
                state = state.copy(showLeaveChatDialog = true)
            }

            is ChatProfileEvent.OnUpdateChat -> {
                state = state.copy(chat = event.chat)
            }

            is ChatProfileEvent.OnAddChatParticipants -> {
                addChatParticipants(event.participants)
            }
        }
    }

    private fun addChatParticipants(participantsToAdd: List<ChatParticipant>) {
        val participants = state.chat?.participants?.toMutableList() ?: mutableListOf()
        val participantIdsToAdd = participantsToAdd.map { it.user.id }
        participants.removeIf { it.user.id in participantIdsToAdd }
        participants.addAll(participantsToAdd)
        state = state.copy(chat = state.chat?.copy(participants = participants))
    }

    private fun leaveChat(
        chatId: Int,
    ) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = leaveChatUseCase(chatId = chatId)) {
                is AppResponse.Success -> {
                    sendUiEvent(ChatProfileUiEvent.OnShowSnackbar(application.getString(R.string.text_left_chat)))
                    sendUiEvent(ChatProfileUiEvent.LeftChat)
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
                this@ChatProfileViewModel.myUser = myUser
            }
        }
    }

    private fun sendUiEvent(event: ChatProfileUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    ChatProfileUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    ChatProfileUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
                sendUiEvent(ChatProfileUiEvent.OnNavigateBack)
            }
        }
    }

}
