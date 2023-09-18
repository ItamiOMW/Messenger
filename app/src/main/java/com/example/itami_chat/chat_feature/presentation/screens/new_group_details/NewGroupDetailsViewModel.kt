package com.example.itami_chat.chat_feature.presentation.screens.new_group_details

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatType
import com.example.itami_chat.chat_feature.domain.use_case.CreateChatUseCase
import com.example.itami_chat.core.domain.exception.EmptyChatNameInputException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.use_case.GetUsersByIdsUseCase
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.utils.toIntList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewGroupDetailsViewModel @Inject constructor(
    private val getUsersByIdsUseCase: GetUsersByIdsUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<NewGroupDetailsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(NewGroupDetailsState())
        private set

    var chatNameState by mutableStateOf(StandardTextFieldState())
        private set

    private lateinit var userIds: List<Int>

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>(Screen.USER_IDS_ARG)?.let { stringIds ->
                val ids = stringIds.toIntList()
                userIds = ids
                if (ids.isNotEmpty()) {
                    getUsers(ids)
                }
            } ?: throw RuntimeException("Argument userIds was not passed")
        }
    }

    fun onEvent(event: NewGroupDetailsEvent) {
        when (event) {
            is NewGroupDetailsEvent.OnChatNameValueChange -> {
                chatNameState = chatNameState.copy(text = event.newValue, errorMessage = null)
            }

            is NewGroupDetailsEvent.OnCreateChat -> {
                createChat(userIds, chatNameState.text, state.chatPictureUri?.toString())
            }

            is NewGroupDetailsEvent.OnPictureUriChange -> {
                state = state.copy(chatPictureUri = event.uri)
            }

            is NewGroupDetailsEvent.OnReloadParticipants -> {
                getUsers(userIds)
            }
        }
    }

    private fun getUsers(userIds: List<Int>) {
        state = state.copy(isLoadingParticipants = true, participantsErrorMessage = null)
        viewModelScope.launch {
            when (val result = getUsersByIdsUseCase(userIds)) {
                is AppResponse.Success -> {
                    state = state.copy(participants = result.data)
                }

                is AppResponse.Failed -> {
                    state = state.copy(
                        participantsErrorMessage = application.getString(R.string.error_failed_to_load_participants)
                    )
                }
            }
            state = state.copy(isLoadingParticipants = false)
        }
    }

    private fun createChat(userIds: List<Int>, name: String, pictureUri: String?) {
        state = state.copy(isCreatingChat = true)
        viewModelScope.launch {
            when (val result = createChatUseCase(name, userIds, pictureUri, ChatType.GROUP)) {
                is AppResponse.Success -> {
                    _uiEvent.send(NewGroupDetailsUiEvent.OnChatCreated)
                }

                is AppResponse.Failed -> {
                    handleException(exception = result.exception, message = result.message)
                }
            }
            state = state.copy(isCreatingChat = false)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {

            is PoorNetworkConnectionException -> {
                showSnackbar(
                    message = application.getString(R.string.error_poor_network_connection)
                )
            }

            is EmptyChatNameInputException -> {
                chatNameState = chatNameState.copy(
                    errorMessage = application.getString(R.string.error_empty_field)
                )
            }

            else -> {
                showSnackbar(
                    message = message ?: application.getString(R.string.error_unknown)
                )
            }

        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            _uiEvent.send(NewGroupDetailsUiEvent.OnShowSnackbar(message = message))
        }
    }
}