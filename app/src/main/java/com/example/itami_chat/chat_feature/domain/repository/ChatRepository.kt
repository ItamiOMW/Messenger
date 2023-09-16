package com.example.itami_chat.chat_feature.domain.repository

import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatEvent
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.ChatType
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.core.domain.model.AppResponse
import kotlinx.coroutines.flow.Flow


interface ChatRepository {

    fun observeChats(): Flow<ChatEvent>

    fun observeChat(chatId: Int): Flow<ChatEvent>

    suspend fun closeChatsConnection()

    suspend fun closeMessagesConnection()

    suspend fun readMessage(id: Int)

    suspend fun sendMessage(
        text: String?,
        pictureUris: List<String>?,
        chatId: Int,
    )

    suspend fun editMessage(id: Int, text: String)

    suspend fun deleteMessage(id: Int)

    suspend fun createChat(
        name: String?,
        userIds: List<Int>,
        chatPictureUri: String?,
        type: ChatType,
    ): AppResponse<Chat>

    suspend fun deleteChat(id: Int): AppResponse<Unit>

    suspend fun updateChat(name: String, chatPictureUri: String?, id: Int): AppResponse<Chat>

    suspend fun leaveChat(chatId: Int): AppResponse<Unit>

    suspend fun addChatParticipants(userIds: List<Int>, chatId: Int): AppResponse<List<ChatParticipant>>

    suspend fun deleteChatParticipant(userId: Int, chatId: Int): AppResponse<Unit>

    suspend fun assignAdminRole(userId: Int, chatId: Int): AppResponse<Unit>

    suspend fun removeAdminRole(userId: Int, chatId: Int): AppResponse<Unit>

    suspend fun getChats(): AppResponse<List<Chat>>

    suspend fun getChatById(chatId: Int): AppResponse<Chat>

    suspend fun getDialogChatByUsers(dialogUserId: Int): AppResponse<Chat>

    suspend fun getMessagesForChat(
        chatId: Int,
        page: Int,
        pageSize: Int,
    ): AppResponse<List<Message>>

}