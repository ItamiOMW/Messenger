package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.domain.model.AppResponse


class AddChatParticipantsUseCase(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(
        userIds: List<Int>,
        chatId: Int,
    ): AppResponse<List<ChatParticipant>> {
        return chatRepository.addChatParticipants(userIds, chatId)
    }

}