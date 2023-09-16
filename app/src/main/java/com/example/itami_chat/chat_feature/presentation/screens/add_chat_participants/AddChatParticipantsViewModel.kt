package com.example.itami_chat.chat_feature.presentation.screens.add_chat_participants

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.domain.use_case.AddChatParticipantsUseCase
import com.example.itami_chat.chat_feature.domain.use_case.GetChatByIdUseCase
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.domain.usecase.GetContactsUseCase
import com.example.itami_chat.core.presentation.navigation.Screen
import com.example.itami_chat.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddChatParticipantsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val getChatByIdUseCase: GetChatByIdUseCase,
    private val addChatParticipantsUseCase: AddChatParticipantsUseCase,
    private val userManager: UserManager,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<AddChatParticipantsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(AddChatParticipantsState())
        private set

    var searchQueryState by mutableStateOf("")
        private set

    private var chatId = Constants.UNKNOWN_ID

    private lateinit var myUser: MyUser


    private var contacts = listOf<SimpleUser>()

    private var filterContactsJob: Job? = null

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
                                AddChatParticipantsUiEvent.OnShowSnackbar(
                                    application.getString(R.string.error_not_chat_participant)
                                )
                            )
                            sendUiEvent(AddChatParticipantsUiEvent.OnNavigateBack(null))
                            return@launch
                        }
                        getContacts()
                        state = state.copy(chat = result.data)
                    }

                    is AppResponse.Failed -> {
                        handleException(exception = result.exception, message = result.message)
                    }
                }
                state = state.copy(isLoading = false)
            } ?: throw RuntimeException("Argument chatId was not passed")
        }
    }

    fun onEvent(event: AddChatParticipantsEvent) {
        when (event) {
            is AddChatParticipantsEvent.OnChangeSearchFieldVisibility -> {
                state = state.copy(isSearchFieldVisible = !state.isSearchFieldVisible)
            }

            is AddChatParticipantsEvent.OnSearchQueryInputChange -> {
                searchQueryState = event.newValue
                filterContacts()
            }

            is AddChatParticipantsEvent.OnSelectUser -> {
                selectUser(event.simpleUser)
            }

            is AddChatParticipantsEvent.OnUpdateContactsList -> {
                getContacts()
            }

            is AddChatParticipantsEvent.OnAddChatParticipants -> {
                addChatParticipants(state.selectedContacts)
            }
        }
    }

    private fun selectUser(simpleUser: SimpleUser) {
        if (state.chat?.participants?.firstOrNull { it.user.id == simpleUser.id } == null) {
            val list = state.selectedContacts.toMutableList()
            val isRemoved = list.removeIf { it.id == simpleUser.id }
            if (!isRemoved) {
                list.add(simpleUser)
            }
            state = state.copy(selectedContacts = list)
        }
    }

    private fun getContacts() {
        state = state.copy(isLoadingContacts = true)
        viewModelScope.launch {
            when (val response = getContactsUseCase()) {
                is AppResponse.Success -> {
                    contacts = response.data
                    filterContacts()
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }
            }
            state = state.copy(isLoadingContacts = false)
        }
    }

    private fun filterContacts() {
        filterContactsJob?.cancel()
        filterContactsJob = viewModelScope.launch {
            val filteredContacts = contacts.filter {
                it.fullName.contains(searchQueryState)
                        || it.username?.contains(searchQueryState) == true
            }
            state = state.copy(contacts = filteredContacts)
        }
    }

    private fun addChatParticipants(participants: List<SimpleUser>) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val participantIds = participants.map { it.id }
            when (val result = addChatParticipantsUseCase(participantIds, chatId)) {
                is AppResponse.Success -> {
                    sendUiEvent(AddChatParticipantsUiEvent.OnNavigateBack(result.data))
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
                this@AddChatParticipantsViewModel.myUser = myUser
            }
        }
    }

    private fun sendUiEvent(event: AddChatParticipantsUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    AddChatParticipantsUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    AddChatParticipantsUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}