package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.repository.ChatRepository

class DeleteMessageUseCase(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(messageId: Int) {
        chatRepository.deleteMessage(messageId)
    }

}