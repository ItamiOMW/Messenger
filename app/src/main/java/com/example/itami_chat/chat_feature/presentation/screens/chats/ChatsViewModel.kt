package com.example.itami_chat.chat_feature.presentation.screens.chats

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatEvent
import com.example.itami_chat.chat_feature.domain.use_case.GetChatsUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ObserveChatEventsUseCase
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.preferences.AppSettingsManager
import com.example.itami_chat.core.domain.preferences.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val observeChatsUseCase: ObserveChatEventsUseCase,
    private val appSettingsManager: AppSettingsManager,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<ChatsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(ChatsState())
        private set

    init {
        getDarkModeState()
        getMyUser()
        getChats()
        observeChats()
    }

    fun onEvent(event: ChatsEvent) {
        when (event) {
            ChatsEvent.ChangeDarkModeState -> {
                changeDarkModeState()
            }
        }
    }

    private fun getChats() {
        viewModelScope.launch {
            when (val response = getChatsUseCase()) {
                is AppResponse.Success -> {
                    state =
                        state.copy(chats = response.data.sortedByDescending { it.lastMessage?.createdAt })
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }
            }
        }
    }

    private fun observeChats() {
        viewModelScope.launch {
            observeChatsUseCase().collect { chatEvent ->
                when (chatEvent) {
                    is ChatEvent.ChatCreated -> {
                        val chats = state.chats.toMutableList()
                        chats.removeIf { it.id == chatEvent.chat.id }
                        chats.add(chatEvent.chat)
                        state =
                            state.copy(chats = chats.sortedByDescending { it.lastMessage?.createdAt })
                    }

                    is ChatEvent.ChatDeleted -> {
                        val chats = state.chats.toMutableList()
                        chats.removeIf { it.id == chatEvent.chat.id }
                        state = state.copy(chats = chats)
                    }

                    is ChatEvent.ChatUpdated -> {
                        val chats = state.chats.toMutableList()
                        chats.removeIf { it.id == chatEvent.chat.id }
                        chats.add(chatEvent.chat)
                        state =
                            state.copy(chats = chats.sortedByDescending { it.lastMessage?.createdAt })
                    }

                    is ChatEvent.Error -> {
                        sendUiEvent(
                            ChatsUiEvent.OnShowSnackbar(
                                chatEvent.message
                            )
                        )
                    }

                    else -> println("Unexpected chat event.")
                }
            }
        }
    }

    private fun changeDarkModeState() {
        viewModelScope.launch {
            appSettingsManager.changeDarkModeState(!state.isDarkMode)
        }
    }

    private fun getDarkModeState() {
        viewModelScope.launch {
            appSettingsManager.isDarkMode.collect { isDarkMode ->
                state = state.copy(isDarkMode = isDarkMode)
            }
        }
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                state = state.copy(myUser = myUser)
            }
        }
    }

    private fun sendUiEvent(event: ChatsUiEvent) {
        viewModelScope.launch {
            sendUiEvent(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                state = state.copy(
                    error = application.getString(R.string.error_poor_network_connection)
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