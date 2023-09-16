package com.example.itami_chat.chat_feature.presentation.screens.edit_chat

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.ParticipantRole
import com.example.itami_chat.chat_feature.domain.use_case.AssignAdminRoleUseCase
import com.example.itami_chat.chat_feature.domain.use_case.DeleteChatParticipantUseCase
import com.example.itami_chat.chat_feature.domain.use_case.EditChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetChatByIdUseCase
import com.example.itami_chat.chat_feature.domain.use_case.RemoveAdminRoleUseCase
import com.example.itami_chat.core.domain.exception.EmptyChatNameInputException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import com.example.itami_chat.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditChatViewModel @Inject constructor(
    private val getChatByIdUseCase: GetChatByIdUseCase,
    private val editChatUseCase: EditChatUseCase,
    private val assignAdminRoleUseCase: AssignAdminRoleUseCase,
    private val removeAdminRoleUseCase: RemoveAdminRoleUseCase,
    private val deleteChatParticipantUseCase: DeleteChatParticipantUseCase,
    private val userManager: UserManager,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiEvent = Channel<EditChatUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(EditChatState())
        private set

    var chatNameState by mutableStateOf(StandardTextFieldState())
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
                                EditChatUiEvent.OnShowSnackbar(
                                    application.getString(R.string.error_not_chat_participant)
                                )
                            )
                            sendUiEvent(EditChatUiEvent.OnNavigateBack(null))
                            return@launch
                        }
                        chatNameState = chatNameState.copy(text = result.data.name)
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

    fun onEvent(event: EditChatEvent) {
        when (event) {
            is EditChatEvent.OnChatNameValueChange -> {
                chatNameState = chatNameState.copy(text = event.newValue, errorMessage = null)
            }

            is EditChatEvent.OnChatPictureUriChange -> {
                state = state.copy(chatPictureUri = event.uri)
            }

            is EditChatEvent.OnDeleteParticipant -> {
                state = state.copy(participantToEdit = null)
                deleteChatParticipant(event.participant, chatId)
            }

            is EditChatEvent.OnEditChat -> {
                editChat(
                    chatNameState.text,
                    state.chatPictureUri?.toString(),
                    chatId
                )
            }

            is EditChatEvent.OnAssignAdminRole -> {
                state = state.copy(participantToEdit = null)
                assignAdminRole(participant = event.participant, chatId = chatId)
            }

            is EditChatEvent.OnRemoveAdminRole -> {
                state = state.copy(participantToEdit = null)
                removeAdminRole(participant = event.participant, chatId = chatId)
            }

            is EditChatEvent.OnHideEditParticipantDialog -> {
                state = state.copy(participantToEdit = null)
            }

            is EditChatEvent.OnShowEditParticipantDialog -> {
                state = state.copy(participantToEdit = event.participant)
            }
        }
    }

    private fun assignAdminRole(participant: ChatParticipant, chatId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result =
                assignAdminRoleUseCase(userId = participant.user.id, chatId = chatId)) {
                is AppResponse.Success -> {
                    val participants = state.chat?.participants?.toMutableList() ?: mutableListOf()
                    val index = participants.indexOfFirst { it.user.id == participant.user.id }
                    val participantToUpdate =
                        participants.find { it.user.id == participant.user.id }
                    participants.removeAt(index)
                    participantToUpdate?.let {
                        participants.add(index, it.copy(role = ParticipantRole.ADMIN))
                    }
                    state = state.copy(chat = state.chat?.copy(participants = participants))
                    sendUiEvent(
                        EditChatUiEvent.OnShowSnackbar(
                            application.getString(
                                R.string.text_admin_role_assigned,
                                participant.user.fullName
                            ),
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    private fun removeAdminRole(participant: ChatParticipant, chatId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result =
                removeAdminRoleUseCase(userId = participant.user.id, chatId = chatId)) {
                is AppResponse.Success -> {
                    val participants = state.chat?.participants?.toMutableList() ?: mutableListOf()
                    val index = participants.indexOfFirst { it.user.id == participant.user.id }
                    val participantToUpdate =
                        participants.find { it.user.id == participant.user.id }
                    participants.removeAt(index)
                    participantToUpdate?.let {
                        participants.add(index, it.copy(role = ParticipantRole.MEMBER))
                    }
                    state = state.copy(chat = state.chat?.copy(participants = participants))
                    sendUiEvent(
                        EditChatUiEvent.OnShowSnackbar(
                            application.getString(
                                R.string.text_admin_role_removed,
                                participant.user.fullName
                            ),
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    private fun deleteChatParticipant(participant: ChatParticipant, chatId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = deleteChatParticipantUseCase(userId = participant.user.id, chatId = chatId)) {
                is AppResponse.Success -> {
                    val participants = state.chat?.participants?.toMutableList() ?: mutableListOf()
                    participants.removeIf { it.user.id == participant.user.id }
                    state = state.copy(chat = state.chat?.copy(participants = participants))
                    sendUiEvent(
                        EditChatUiEvent.OnShowSnackbar(
                            application.getString(
                                R.string.text_user_was_deleted_from_chat,
                                participant.user.fullName
                            ),
                        )
                    )
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    private fun editChat(
        name: String,
        pictureUri: String?,
        chatId: Int,
    ) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = editChatUseCase(name, pictureUri, chatId)) {
                is AppResponse.Success -> {
                    state = state.copy(chat = result.data)
                    sendUiEvent(
                        EditChatUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_chat_edited_successfully)
                        )
                    )
                    sendUiEvent(EditChatUiEvent.OnNavigateBack(result.data))
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
                this@EditChatViewModel.myUser = myUser
            }
        }
    }

    private fun sendUiEvent(event: EditChatUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    EditChatUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            is EmptyChatNameInputException -> {
                chatNameState = chatNameState.copy(
                    errorMessage = application.getString(R.string.error_empty_field)
                )
            }

            else -> {
                sendUiEvent(
                    EditChatUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}