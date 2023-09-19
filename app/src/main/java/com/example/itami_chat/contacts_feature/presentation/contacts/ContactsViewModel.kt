package com.example.itami_chat.contacts_feature.presentation.contacts

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.contacts_feature.domain.use_case.GetContactRequestsUseCase
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.domain.preferences.AppSettingsManager
import com.example.itami_chat.core.domain.preferences.UserManager
import com.example.itami_chat.core.domain.use_case.AcceptContactRequestUseCase
import com.example.itami_chat.core.domain.use_case.CancelContactRequestUseCase
import com.example.itami_chat.core.domain.use_case.DeclineContactRequestUseCase
import com.example.itami_chat.core.domain.use_case.DeleteContactUseCase
import com.example.itami_chat.core.domain.use_case.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val getContactRequestsUseCase: GetContactRequestsUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val acceptContactRequestUseCase: AcceptContactRequestUseCase,
    private val declineContactRequestUseCase: DeclineContactRequestUseCase,
    private val cancelContactRequestUseCase: CancelContactRequestUseCase,
    private val appSettingsManager: AppSettingsManager,
    private val userManager: UserManager,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<ContactsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(ContactsState())
        private set

    var theme by mutableStateOf(Theme.Default)
        private set

    var isDarkMode by mutableStateOf(false)
        private set

    var searchContactsQueryState by mutableStateOf("")
        private set

    private lateinit var myUser: MyUser

    private var contacts: List<SimpleUser> = emptyList()

    private var filterContactsJob: Job? = null

    init {
        getMyUser()
        getCurrentTheme()
        getDarkModeState()
        getContacts()
        getContactRequests()
    }

    fun onEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.OnAcceptContactRequest -> {
                acceptContactRequest(event.id)
            }

            is ContactsEvent.OnCancelContactRequest -> {
                cancelContactRequest(event.id)
            }

            is ContactsEvent.OnDeclineContactRequest -> {
                declineContactRequest(event.id)
            }

            is ContactsEvent.OnDeleteContact -> {
                deleteContact(event.id)
            }

            is ContactsEvent.OnRefreshContacts -> {
                getContacts()
            }

            is ContactsEvent.OnRefreshContactRequests -> {
                getContactRequests()
            }

            is ContactsEvent.OnSearchContactsQueryChange -> {
                searchContactsQueryState = event.newValue
                filterContacts()
            }

            ContactsEvent.OnChangeSearchFieldVisibility -> {
                state = state.copy(isSearchFieldVisible = !state.isSearchFieldVisible)
            }
        }
    }

    private fun acceptContactRequest(requestId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = acceptContactRequestUseCase(requestId)) {
                is AppResponse.Success -> {
                    val contactRequests = state.contactRequests.toMutableList()
                    val contactRequest =
                        contactRequests.find { it.id == requestId } ?: return@launch
                    contactRequests.removeIf { it.id == requestId }
                    state = state.copy(contactRequests = contactRequests)

                    val contacts = contacts.toMutableList().apply {
                        removeIf { it.id == contactRequest.sender.id }
                        add(contactRequest.sender)
                    }
                    this@ContactsViewModel.contacts = contacts
                    filterContacts()

                    sendUiEvent(
                        ContactsUiEvent.OnShowSnackbar(
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
                    val contactRequests = state.contactRequests.toMutableList().apply {
                        removeIf { it.id == requestId }
                    }
                    state = state.copy(contactRequests = contactRequests)
                    sendUiEvent(
                        ContactsUiEvent.OnShowSnackbar(
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
                    val myContactRequests = state.myContactRequests.toMutableList().apply {
                        removeIf { it.id == requestId }
                    }
                    state = state.copy(myContactRequests = myContactRequests)
                    sendUiEvent(
                        ContactsUiEvent.OnShowSnackbar(
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
                    val contacts = contacts.toMutableList().apply {
                        removeIf { it.id == userId }
                    }
                    this@ContactsViewModel.contacts = contacts
                    filterContacts()
                    sendUiEvent(
                        ContactsUiEvent.OnShowSnackbar(
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

    private fun getContactRequests() {
        state = state.copy(isLoadingContacts = true)
        viewModelScope.launch {
            when (val response = getContactRequestsUseCase()) {
                is AppResponse.Success -> {
                    val myContactRequests = response.data.filter { it.sender.id == myUser.id }
                    val contactRequests = response.data.filter { it.recipient.id == myUser.id }
                    state = state.copy(
                        contactRequests = contactRequests,
                        myContactRequests = myContactRequests
                    )
                }

                is AppResponse.Failed -> {
                    handleException(response.exception, response.message)
                }
            }
            state = state.copy(isLoadingContacts = false)
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
                it.fullName.contains(searchContactsQueryState)
                        || it.username?.contains(searchContactsQueryState) == true
            }
            state = state.copy(contacts = filteredContacts)
        }
    }

    private fun getMyUser() {
        viewModelScope.launch {
            userManager.user.collect { myUser ->
                this@ContactsViewModel.myUser = myUser
            }
        }
    }

    private fun getCurrentTheme() {
        viewModelScope.launch {
            appSettingsManager.currentTheme.collect { theme ->
                this@ContactsViewModel.theme = theme
            }
        }
    }

    private fun getDarkModeState() {
        viewModelScope.launch {
            appSettingsManager.isDarkMode.collect { isDarkMode ->
                this@ContactsViewModel.isDarkMode = isDarkMode
            }
        }
    }

    private fun sendUiEvent(event: ContactsUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    ContactsUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    ContactsUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }
}