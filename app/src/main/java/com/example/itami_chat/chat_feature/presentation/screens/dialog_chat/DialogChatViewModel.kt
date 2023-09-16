package com.example.itami_chat.chat_feature.presentation.screens.dialog_chat

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatEvent
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.use_case.DeleteChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.DeleteMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.EditMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetDialogChatByUsersUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetMessagesForChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ObserveChatUseCase
import com.example.itami_chat.chat_feature.domain.use_case.ReadMessageUseCase
import com.example.itami_chat.chat_feature.domain.use_case.SendMessageUseCase
import com.example.itami_chat.core.domain.exception.EmptyMessageInputException
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.paginator.DefaultPaginator
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.presentation.state.StandardTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DialogChatViewModel @Inject constructor(
    private val getMessagesForChatUseCase: GetMessagesForChatUseCase,
    private val observeChatUseCase: ObserveChatUseCase,
    private val getDialogChatByUsersUseCase: GetDialogChatByUsersUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val readMessageUseCase: ReadMessageUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<DialogChatUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(DialogChatState())
        private set

    var messageInputState by mutableStateOf(StandardTextFieldState())
        private set

    var editMessageInputState by mutableStateOf(StandardTextFieldState())
        private set

    private lateinit var chat: Chat

    init {
        viewModelScope.launch {
            getMyUser()
            savedStateHandle.get<Int>(Screen.USER_ID_ARG)?.let { id ->
                when (val result = getDialogChatByUsersUseCase(id)) {
                    is AppResponse.Success -> {
                        chat = result.data
                        state = state.copy(
                            dialogUser = chat.participants.find { it.user.id == id }
                        )
                        loadNextMessages()
                        observeChat(chat.id)
                    }

                    is AppResponse.Failed -> {
                        handleException(exception = result.exception, message = result.message)
                    }
                }
            } ?: throw RuntimeException("Argument userId was not passed")
        }
    }

    private val paginator = DefaultPaginator(
        initialKey = state.messagesPage,
        onLoadUpdated = { state = state.copy(isLoadingNextMessages = it) },
        onRequest = { nextPage -> getMessagesForChatUseCase(chat.id, state.messagesPage, 20) },
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


    fun onEvent(event: DialogChatEvent) {
        when (event) {
            is DialogChatEvent.OnMessageInputValueChange -> {
                messageInputState = messageInputState.copy(
                    text = event.newValue,
                    errorMessage = null
                )
            }

            is DialogChatEvent.OnEditMessageInputValueChange -> {
                editMessageInputState = editMessageInputState.copy(
                    text = event.newValue,
                    errorMessage = null
                )
            }

            is DialogChatEvent.OnSendMessage -> {
                sendMessage(
                    chatId = chat.id,
                    pictureUris = null,
                    text = messageInputState.text
                )
            }

            is DialogChatEvent.OnDeleteChat -> {
                state = state.copy(showDeleteChatDialog = false)
                deleteChat(chatId = chat.id)
            }

            is DialogChatEvent.OnLoadNextMessages -> {
                loadNextMessages()
            }

            is DialogChatEvent.OnDeleteMessage -> {
                state = state.copy(messageToDelete = null, showDeleteMessageDialog = false)
                deleteMessage(event.message.id)
            }

            is DialogChatEvent.OnEditMessage -> {
                state = state.copy(messageToEdit = null)
                editMessage(message = event.message)
            }

            is DialogChatEvent.OnShowDeleteMessageDialog -> {
                state = state.copy(messageToDelete = event.message, showDeleteMessageDialog = true)
            }

            is DialogChatEvent.OnHideDeleteMessageDialog -> {
                state = state.copy(messageToDelete = null, showDeleteMessageDialog = false)
            }

            is DialogChatEvent.OnShowEditMessageTextField -> {
                editMessageInputState = editMessageInputState.copy(
                    text = event.message.text ?: "",
                    errorMessage = null
                )
                state = state.copy(messageToEdit = event.message)
            }

            is DialogChatEvent.OnHideEditMessageTextField -> {
                state = state.copy(messageToEdit = null)
            }

            is DialogChatEvent.OnHideDeleteChatDialog -> {
                state = state.copy(showDeleteChatDialog = false)
            }

            is DialogChatEvent.OnShowDeleteChatDialog -> {
                state = state.copy(showDeleteChatDialog = true)
            }

            is DialogChatEvent.OnReadMessage -> {
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

    private fun deleteChat(
        chatId: Int,
    ) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = deleteChatUseCase(chatId = chatId)) {
                is AppResponse.Success -> {
                    sendUiEvent(
                        DialogChatUiEvent.ChatDeleted(
                            application.getString(R.string.text_chat_was_deleted)
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
                        sendUiEvent(DialogChatUiEvent.NewMessage(chatEvent.message))
                    }

                    is ChatEvent.MessageUpdated -> {
                        updateMessageInList(message = chatEvent.message)
                    }

                    is ChatEvent.ChatDeleted -> {
                        sendUiEvent(
                            DialogChatUiEvent.ChatDeleted(
                                application.getString(R.string.text_chat_was_deleted)
                            )
                        )
                    }

                    else -> Unit //Unexpected events in Dialog Chat
                }
            }
        }
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
                state = state.copy(myUserId = myUser.id)
            }
        }
    }

    private fun sendUiEvent(event: DialogChatUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    DialogChatUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            is EmptyMessageInputException -> {
                sendUiEvent(DialogChatUiEvent.OnShowSnackbar(application.getString(R.string.error_empty_field)))
            }

            else -> {
                sendUiEvent(
                    DialogChatUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}