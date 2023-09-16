package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.model.ChatEvent
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ObserveChatEventsUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<ChatEvent> {
        return chatRepository.observeChats()
    }

}