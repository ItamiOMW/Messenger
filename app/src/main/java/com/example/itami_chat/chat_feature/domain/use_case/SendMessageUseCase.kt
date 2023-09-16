package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.domain.exception.EmptyMessageInputException

class SendMessageUseCase(
    private val chatRepository: ChatRepository,
) {

    suspend operator fun invoke(
        chatId: Int,
        text: String?,
        pictureUris: List<String>?,
    ) {
        val trimmedText = text?.trim()
        if (trimmedText == null && pictureUris == null) {
            throw EmptyMessageInputException
        }

        chatRepository.sendMessage(
            text = trimmedText,
            pictureUris = pictureUris,
            chatId = chatId
        )

    }

}