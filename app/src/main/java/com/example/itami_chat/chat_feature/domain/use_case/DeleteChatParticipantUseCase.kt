package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.domain.model.AppResponse


class DeleteChatParticipantUseCase(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(userId: Int, chatId: Int): AppResponse<Unit> {
        return chatRepository.deleteChatParticipant(userId, chatId)
    }

}