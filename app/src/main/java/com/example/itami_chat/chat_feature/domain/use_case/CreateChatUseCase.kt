package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatType
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.domain.exception.EmptyChatNameInputException
import com.example.itami_chat.core.domain.model.AppResponse

class CreateChatUseCase(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(
        name: String,
        userIds: List<Int>,
        pictureUri: String?,
        type: ChatType,
    ): AppResponse<Chat> {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            return AppResponse.failed(EmptyChatNameInputException)
        }

        return chatRepository.createChat(trimmedName, userIds, pictureUri, type)
    }

}