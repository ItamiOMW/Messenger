package com.example.itami_chat.chat_feature.presentation.screens.new_group_participants

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.usecase.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class NewGroupParticipantsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<NewGroupParticipantsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(NewGroupParticipantsState())
        private set

    var searchQueryState by mutableStateOf("")
        private set

    private var contacts = listOf<SimpleUser>()

    private var filterContactsJob: Job? = null

    init {
        getContacts()
    }

    fun onEvent(event: NewGroupParticipantsEvent) {
        when (event) {
            is NewGroupParticipantsEvent.OnChangeSearchFieldVisibility -> {
                state = state.copy(isSearchFieldVisible = !state.isSearchFieldVisible)
            }

            is NewGroupParticipantsEvent.OnSearchQueryInputChange -> {
                searchQueryState = event.newValue
                filterContacts()
            }

            is NewGroupParticipantsEvent.OnSelectUser -> {
                selectUser(event.simpleUser)
            }

            is NewGroupParticipantsEvent.OnUpdateContactsList -> {
                getContacts()
            }
        }
    }

    private fun selectUser(simpleUser: SimpleUser) {
        val list = state.selectedContacts.toMutableList()

        val isRemoved = list.removeIf { it.id == simpleUser.id }
        if (!isRemoved) {
            list.add(simpleUser)
        }
        state = state.copy(selectedContacts = list)
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

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {

            is PoorNetworkConnectionException -> {
                showSnackbar(
                    application.getString(R.string.error_poor_network_connection)
                )
            }

            else -> {
                showSnackbar(
                    message ?: application.getString(R.string.error_unknown)
                )
            }

        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            _uiEvent.send(NewGroupParticipantsUiEvent.OnShowSnackbar(message = message))
        }
    }

}