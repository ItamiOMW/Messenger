package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.domain.exception.EmptyChatNameInputException
import com.example.itami_chat.core.domain.model.AppResponse


class EditChatUseCase(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(
        name: String,
        chatPictureUri: String?,
        chatId: Int,
    ): AppResponse<Chat> {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) {
            return AppResponse.failed(EmptyChatNameInputException)
        }

        return chatRepository.updateChat(name, chatPictureUri, chatId)
    }

}