package com.example.itami_chat.profile_feature.presentation.search_users

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.R
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.use_case.SearchForUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val searchForUsersUseCase: SearchForUsersUseCase,
    private val application: Application,
) : ViewModel() {

    private val _uiEvent = Channel<SearchUsersUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SearchUsersState())
        private set

    var searchInputState by mutableStateOf("")
        private set

    private var searchUsersJob: Job? = null


    fun onEvent(event: SearchUsersEvent) {
        when (event) {
            is SearchUsersEvent.OnSearchInputChange -> {
                searchInputState = event.newValue
                searchUsersJob?.cancel()
                searchUsersJob = viewModelScope.launch {
                    delay(500)
                    searchForUsers(event.newValue)
                }
            }
        }
    }

    private suspend fun searchForUsers(query: String) {
        state = state.copy(isLoading = true)
        when (val result = searchForUsersUseCase(query)) {
            is AppResponse.Success -> {
                state = state.copy(
                    users = result.data,
                )
            }

            is AppResponse.Failed -> {
                handleException(exception = result.exception, message = result.message)
            }
        }
        state = state.copy(isLoading = false)
    }

    private fun sendUiEvent(event: SearchUsersUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun handleException(exception: Exception, message: String?) {
        when (exception) {
            is PoorNetworkConnectionException -> {
                sendUiEvent(
                    SearchUsersUiEvent.OnShowSnackbar(
                        application.getString(R.string.error_poor_network_connection)
                    )
                )
            }

            else -> {
                sendUiEvent(
                    SearchUsersUiEvent.OnShowSnackbar(
                        message = message ?: application.getString(
                            R.string.error_unknown
                        )
                    )
                )
            }
        }
    }

}