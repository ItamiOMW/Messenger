package com.example.itami_chat.chat_feature.domain.use_case

import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.domain.model.AppResponse

class GetMessagesForChatUseCase(
    private val chatRepository: ChatRepository,
) {

    suspend operator fun invoke(
        chatId: Int,
        page: Int,
        pageSize: Int,
    ): AppResponse<List<Message>> {
        return chatRepository.getMessagesForChat(
            chatId = chatId,
            page = page,
            pageSize = pageSize
        )
    }

}