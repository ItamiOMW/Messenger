package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.domain.model.AppResponse

class GetChatByIdUseCase(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(chatId: Int): AppResponse<Chat> {
        return chatRepository.getChatById(chatId)
    }

}