package com.example.itami_chat.chat_feature.presentation.screens.group_chat

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.ChatEvent
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.use_case.DeleteMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.EditMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetChatByIdUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetMessagesForChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.LeaveChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ObserveChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ReadMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.SendMessageUseCase
import com.example.itami_chat.core.domain.exception.EmptyMessageInputException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.paginator.DefaultPaginator
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
class GroupChatViewModel @Inject constructor(
    private val getMessagesForChatUseCase: GetMessagesForChatUseCase,
    private val observeChatUseCase: ObserveChatUseCase,
    private val getChatByIdUseCase: GetChatByIdUseCase,
    private val leaveChatUseCase: LeaveChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val readMessageUseCase: ReadMessageUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<GroupChatUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(GroupChatState())
        private set

    var messageInputState by mutableStateOf(StandardTextFieldState())
        private set

    var editMessageInputState by mutableStateOf(StandardTextFieldState())
        private set

    private var chatId: Int = Constants.UNKNOWN_ID

    private lateinit var myUser: MyUser

    init {
        viewModelScope.launch {
            getMyUser()
            savedStateHandle.get<Int>(Screen.CHAT_ID_ARG)?.let { id ->
                when (val result = getChatByIdUseCase(id)) {
                    is AppResponse.Success -> {
                        chatId = result.data.id
                        val me = result.data.participants.find { it.user.id == myUser.id }
                        if (me == null) {
                            sendUiEvent(
                                GroupChatUiEvent.OnShowSnackbar(
                                    application.getString(R.string.error_not_chat_participant)
                                )
                            )
                            sendUiEvent(GroupChatUiEvent.OnNavigateBack)
                            return@launch
                        }
                        state = state.copy(chat = result.data, me = me)
                        loadNextMessages()
                        observeChat(chatId)
                    }

                    is AppResponse.Failed -> {
                        handleException(exception = result.exception, message = result.message)
                    }
                }
            } ?: throw RuntimeException("Argument chatId was not passed")
        }
    }

    private val paginator = DefaultPaginator(
        initialKey = state.messagesPage,
        onLoadUpdated = { state = state.copy(isLoadingNextMessages = it) },
        onRequest = { nextPage -> getMessagesForChatUseCase(chatId, state.messagesPage, 20) },
        getNextKey = { state.messagesPage + 1 },
        onError = { exception ->
            handleException(exception = exception, message = null)
        },
        onSuccess = { messages, newKey ->
            state = state.copy(
                messagesPage = newKey,
                endReached = messages.isEmpty()
            )
            addMessagesToList(messages)
        }
    )


    fun onEvent(event: GroupChatEvent) {
        when (event) {
            is GroupChatEvent.OnMessageInputValueChange -> {
                messageInputState = messageInputState.copy(
                    text = event.newValue,
                    errorMessage = null
                )
            }

            is GroupChatEvent.OnEditMessageInputValueChange -> {
                editMessageInputState = editMessageInputState.copy(
                    text = event.newValue,
                    errorMessage = null
                )
            }

            is GroupChatEvent.OnSendMessage -> {
                sendMessage(
                    chatId = chatId,
                    pictureUris = null,
                    text = messageInputState.text
                )
            }

            is GroupChatEvent.OnLeaveChat -> {
                state = state.copy(showLeaveChatDialog = false)
                leaveChat(chatId = chatId)
            }

            is GroupChatEvent.OnLoadNextMessages -> {
                loadNextMessages()
            }

            is GroupChatEvent.OnDeleteMessage -> {
                state = state.copy(messageToDelete = null, showDeleteMessageDialog = false)
                deleteMessage(event.message.id)
            }

            is GroupChatEvent.OnEditMessage -> {
                state = state.copy(messageToEdit = null)
                editMessage(message = event.message)
            }

            is GroupChatEvent.OnShowDeleteMessageDialog -> {
                state = state.copy(messageToDelete = event.message, showDeleteMessageDialog = true)
            }

            is GroupChatEvent.OnHideDeleteMessageDialog -> {
                state = state.copy(messageToDelete = null, showDeleteMessageDialog = false)
            }

            is GroupChatEvent.OnShowEditMessageTextField -> {
                editMessageInputState = editMessageInputState.copy(
                    text = event.message.text ?: "",
                    errorMessage = null
                )
                state = state.copy(messageToEdit = event.message)
            }

            is GroupChatEvent.OnHideEditMessageTextField -> {
                state = state.copy(messageToEdit = null)
            }

            is GroupChatEvent.OnHideLeaveChatDialog -> {
                state = state.copy(showLeaveChatDialog = false)
            }

            is GroupChatEvent.OnShowLeaveChatDialog -> {
                state = state.copy(showLeaveChatDialog = true)
            }

            is GroupChatEvent.OnReadMessage -> {
                readMessage(event.messageId)
            }
        }
    }

    private fun readMessage(id: Int) {
        viewModelScope.launch {
            readMessageUseCase(id)
        }
    }

    private fun deleteMessage(id: Int) {
        viewModelScope.launch {
            deleteMessageUseCase(id)
        }
    }

    private fun editMessage(message: Message) {
        viewModelScope.launch {
            editMessageUseCase(message.id, message.text ?: "")
        }
    }

    private fun sendMessage(
        chatId: Int,
        pictureUris: List<String>?,
        text: String,
    ) {
        state = state.copy(isSendingMessage = true)
        viewModelScope.launch {
            sendMessageUseCase(
                chatId = chatId,
                text = text,
                pictureUris = pictureUris
            )
            messageInputState = messageInputState.copy(text = "")
            state = state.copy(isSendingMessage = false)
        }
    }

    private fun leaveChat(
        chatId: Int,
    ) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = leaveChatUseCase(chatId = chatId)) {
                is AppResponse.Success -> {
                    sendUiEvent(
                        GroupChatUiEvent.OnShowSnackbar(
                            application.getString(R.string.text_left_chat)
                        )
                    )
                    sendUiEvent(GroupChatUiEvent.OnNavigateBack)
                }

                is AppResponse.Failed -> {
                    handleException(result.exception, result.message)
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    private fun loadNextMessages() {
        viewModelScope.launch {
            state = state.copy(errorMessage = null)
            paginator.loadNextPage()
        }
    }

    private fun observeChat(chatId: Int) {
        viewModelScope.launch {
            observeChatUseCase(chatId).collect { chatEvent ->
                when (chatEvent) {
                    is ChatEvent.MessageDeleted -> {
                        deleteMessageFromList(chatEvent.message)
                    }

                    is ChatEvent.MessageSent -> {
                        addNewMessageToList(chatEvent.message)
                        sendUiEvent(GroupChatUiEvent.NewMessage(chatEvent.message))
                    }

                    is ChatEvent.MessageUpdated -> {
                        updateMessageInList(message = chatEvent.message)
                    }

                    is ChatEvent.ChatUpdated -> {
                        val me = chatEvent.chat.participants.find { it.user.id == myUser.id }
                        state = state.copy(chat = chatEvent.chat, me = me)
                    }

                    is ChatEvent.ChatDeleted -> {
                        sendUiEvent(
                            GroupChatUiEvent.OnShowSnackbar(
                                application.getString(R.string.text_chat_was_deleted)
                            )
                        )
                    }

                    is ChatEvent.LeftChat -> {
                        if (chatEvent.userLeftId == myUser.id) {
                            sendUiEvent(
                                GroupChatUiEvent.OnShowSnackbar(
                                    application.getString(R.string.text_left_chat)
                                )
                            )
                            sendUiEvent(GroupChatUiEvent.OnNavigateBack)
                        }
                        val participants = state.chat?.participants?.toMutableList()
                        participants?.removeIf { it.user.id == chatEvent.userLeftId }
                        state = state.copy(chat = state.chat?.copy(participants = participants ?: emptyList()))
                    }

                    is ChatEvent.AddChatParticipants -> {
                        addChatParticipants(chatEvent.participants)
                    }

                    is ChatEvent.DeleteChatParticipant -> {
                        if (state.me?.user?.id == chatEvent.participantId) {
                            sendUiEvent(
                                GroupChatUiEvent.OnShowSnackbar(
                                    application.getString(R.string.text_kicked_from_chat)
                                )
                            )
                            sendUiEvent(GroupChatUiEvent.OnNavigateBack)
                        }
                        deleteChatParticipant(chatEvent.participantId)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun addChatParticipants(participantsToAdd: List<ChatParticipant>) {
        val participants = state.chat?.participants?.toMutableList() ?: mutableListOf()
        val participantIdsToAdd = participantsToAdd.map { it.user.id }
        participants.removeIf { it.user.id in participantIdsToAdd }
        participants.addAll(participants)
        state = state.copy(chat = state.chat?.copy(participants = participants))
    }

    private fun deleteChatParticipant(participantId: Int) {
        val participants = state.chat?.participants?.toMutableList() ?: mutableListOf()
        participants.removeIf { it.user.id == participantId }
        state = state.copy(chat = state.chat?.copy(participants = participants))
    }

    private fun addMessagesToList(messages: List<Message>) {
        val list = state.messages.toMutableList()
        val newMessagesIds = messages.map { it.id }
        list.removeAll { it.id in newMessagesIds }
        state = state.copy(messages = list + messages)
    }

    private fun addNewMessageToList(message: Message) {
        val list = state.messages.toMutableList()
        list.add(0, message)
        state = state.copy(messages = list)
    }

    private fun updateMessageInList(message: Message) {
        val list = state.messages.toMutableList()
        val messageToUpdate = list.find { it.id == message.id } ?: return
        val index = list.indexOf(messageToUpdate)
        list.removeAt(index)
        list.add(index, message)
        state = state.copy(messages = list)
    }

    private fun deleteMessageFromList(message: Message) {
        val list = state.messages.toMutableList()
        list.removeIf { it.id == message.id }
        state = state.copy(messages = list)
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                this@GroupChatViewModel.myUser = myUser
            }
        }
    }

    private fun sendUiEvent(event: GroupChatUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    GroupChatUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            is EmptyMessageInputException -> {
                sendUiEvent(GroupChatUiEvent.OnShowSnackbar(application.getString(R.string.error_empty_field)))
            }

            else -> {
                sendUiEvent(
                    GroupChatUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}