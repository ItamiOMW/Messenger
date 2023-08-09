package com.example.itami_chat.chat_feature.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {


    private val _uiEvent = Channel<ChatsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    //Temporary method to test AuthFeature

    fun onEvent(event: ChatsEvent) {
        when(event) {
            ChatsEvent.OnLogout -> {
                logout()
            }
        }
    }
    private fun logout() {
        viewModelScope.launch {
            when (val response = userRepository.logout()) {
                is AppResponse.Success -> {
                    _uiEvent.send(ChatsUiEvent.OnLogoutSuccessful)
                }

                is AppResponse.Failed -> {

                }
            }
        }
    }

}