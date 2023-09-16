package com.example.itami_chat.chat_feature.data.repository

import android.app.Application
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.itami_chat.R
import com.example.itami_chat.chat_feature.data.mapper.toChat
import com.example.itami_chat.chat_feature.data.mapper.toChatParticipant
import com.example.itami_chat.chat_feature.data.mapper.toMessage
import com.example.itami_chat.chat_feature.data.remote.ChatApiService
import com.example.itami_chat.chat_feature.data.remote.dto.ChatParticipantResponse
import com.example.itami_chat.chat_feature.data.remote.dto.ChatResponse
import com.example.itami_chat.chat_feature.data.remote.dto.CreateChatRequest
import com.example.itami_chat.chat_feature.data.remote.dto.DeleteMessageRequest
import com.example.itami_chat.chat_feature.data.remote.dto.EditMessageRequest
import com.example.itami_chat.chat_feature.data.remote.dto.MessageResponse
import com.example.itami_chat.chat_feature.data.remote.dto.ReadMessageRequest
import com.example.itami_chat.chat_feature.data.remote.dto.SendMessageRequest
import com.example.itami_chat.chat_feature.data.remote.dto.UpdateChatRequest
import com.example.itami_chat.chat_feature.data.remote.util.WebSocketEvent
import com.example.itami_chat.chat_feature.domain.model.Chat
import com.example.itami_chat.chat_feature.domain.model.ChatEvent
import com.example.itami_chat.chat_feature.domain.model.ChatParticipant
import com.example.itami_chat.chat_feature.domain.model.ChatType
import com.example.itami_chat.chat_feature.domain.model.Message
import com.example.itami_chat.chat_feature.domain.model.MessageType
import com.example.itami_chat.chat_feature.domain.repository.ChatRepository
import com.example.itami_chat.core.data.preferences.auth.AuthManager
import com.example.itami_chat.core.data.remote.dto.response.FailedApiResponse
import com.example.itami_chat.core.domain.exception.PoorNetworkConnectionException
import com.example.itami_chat.core.domain.exception.ServerErrorException
import com.example.itami_chat.core.domain.exception.UnauthorizedException
import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.utils.NetworkUtil
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val authManager: AuthManager,
    private val chatApiService: ChatApiService,
    private val client: HttpClient,
    private val application: Application,
    private val gson: Gson,
) : ChatRepository {

    private var chatsSession: WebSocketSession? = null
    private var messagesSession: WebSocketSession? = null

    override fun observeChats(): Flow<ChatEvent> {
        return flow {
            chatsSession = client.webSocketSession {
                val token = authManager.token ?: throw UnauthorizedException
                headers.append("Authorization", "Bearer $token")
                url("ws://192.168.171.188:8000/api/v1/chats/ws")
            }
            val chatEvent = chatsSession
                ?.incoming
                ?.consumeAsFlow()
                ?.filterIsInstance<Frame.Text>()
                ?.mapNotNull {
                    val frameText = it.readText()
                    handleWebSocketEvent(gson, frameText)
                }

            if (chatEvent != null) {
                emitAll(chatEvent)
            }
        }
    }

    override suspend fun closeChatsConnection() {
        chatsSession?.close()
        chatsSession = null
    }

    override fun observeChat(chatId: Int): Flow<ChatEvent> {
        return flow {
            messagesSession = client.webSocketSession {
                val token = authManager.token ?: throw UnauthorizedException
                headers.append("Authorization", "Bearer $token")
                url("ws://192.168.171.188:8000/api/v1/chats/$chatId/ws")
            }
            val chatEvent = messagesSession
                ?.incoming
                ?.consumeAsFlow()
                ?.filterIsInstance<Frame.Text>()
                ?.mapNotNull {
                    val frameText = it.readText()
                    handleWebSocketEvent(gson, frameText)
                }

            if (chatEvent != null) {
                emitAll(chatEvent)
            }
        }
    }

    override suspend fun closeMessagesConnection() {
        messagesSession?.close()
        messagesSession = null
    }

    override suspend fun readMessage(id: Int) {
        val readMessageRequest = ReadMessageRequest(id)
        val readMessageJson = gson.toJson(readMessageRequest)
        messagesSession?.outgoing?.send(
            Frame.Text("${WebSocketEvent.READ_MESSAGE}#$readMessageJson")
        )
    }

    override suspend fun sendMessage(
        text: String?,
        pictureUris: List<String>?,
        chatId: Int,
    ) {
        val sendMessageRequest = SendMessageRequest(
            chatId = chatId,
            text = text,
            type = MessageType.MESSAGE
        )
        val sendMessageJson = gson.toJson(sendMessageRequest)
        messagesSession?.outgoing?.send(
            Frame.Text("${WebSocketEvent.SEND_MESSAGE}#$sendMessageJson")
        )
    }

    override suspend fun editMessage(id: Int, text: String) {
        val editMessageRequest = EditMessageRequest(id = id, text)
        val editMessageJson = gson.toJson(editMessageRequest)
        messagesSession?.outgoing?.send(
            Frame.Text("${WebSocketEvent.EDIT_MESSAGE}#$editMessageJson")
        )
    }

    override suspend fun deleteMessage(id: Int) {
        val deleteMessageRequest = DeleteMessageRequest(id = id)
        val deleteMessageJson = gson.toJson(deleteMessageRequest)
        messagesSession?.outgoing?.send(
            Frame.Text("${WebSocketEvent.DELETE_MESSAGE}#$deleteMessageJson")
        )
    }

    override suspend fun createChat(
        name: String?,
        userIds: List<Int>,
        chatPictureUri: String?,
        type: ChatType,
    ): AppResponse<Chat> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val createChatRequest = CreateChatRequest(name, type, userIds)
            val chatPictureFile = chatPictureUri?.toUri()?.toFile()

            val response = chatApiService.createChat(
                token = "Bearer $token",
                createChatRequest = MultipartBody.Part
                    .createFormData(
                        name = "create_chat_request",
                        value = gson.toJson(createChatRequest)
                    ),
                chatPicture = chatPictureFile?.let { file ->
                    MultipartBody.Part
                        .createFormData(
                            name = "chat_picture",
                            filename = file.name,
                            file.asRequestBody()
                        )
                }
            )

            if (response.isSuccessful) {
                val chat = response.body()?.data?.toChat() ?: throw ServerErrorException
                return AppResponse.success(chat)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun deleteChat(id: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.deleteChat("Bearer $token", id)
            if (response.isSuccessful) {
                return AppResponse.success(Unit)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun updateChat(
        name: String,
        chatPictureUri: String?,
        id: Int,
    ): AppResponse<Chat> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val updateChatRequest = UpdateChatRequest(name)
            val chatPictureFile = chatPictureUri?.toUri()?.toFile()

            val response = chatApiService.updateChat(
                token = "Bearer $token",
                chatId = id,
                updateChatRequest = MultipartBody.Part
                    .createFormData(
                        name = "update_chat_request",
                        value = gson.toJson(updateChatRequest)
                    ),
                chatPicture = chatPictureFile?.let { file ->
                    MultipartBody.Part
                        .createFormData(
                            name = "chat_picture",
                            filename = file.name,
                            file.asRequestBody()
                        )
                }
            )

            if (response.isSuccessful) {
                val chat = response.body()?.data?.toChat() ?: throw ServerErrorException
                return AppResponse.success(chat)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun leaveChat(chatId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.leaveChat("Bearer $token", chatId)
            if (response.isSuccessful) {
                return AppResponse.success(Unit)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun addChatParticipants(
        userIds: List<Int>,
        chatId: Int,
    ): AppResponse<List<ChatParticipant>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.addChatParticipants("Bearer $token", chatId, userIds)
            if (response.isSuccessful) {
                val participants =
                    response.body()?.data?.map { it.toChatParticipant() } ?: emptyList()
                return AppResponse.success(participants)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun deleteChatParticipant(userId: Int, chatId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.deleteChatParticipant("Bearer $token", chatId, userId)
            if (response.isSuccessful) {
                return AppResponse.success(Unit)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun assignAdminRole(userId: Int, chatId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.assignAdminRole("Bearer $token", chatId, userId)
            if (response.isSuccessful) {
                return AppResponse.success(Unit)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun removeAdminRole(userId: Int, chatId: Int): AppResponse<Unit> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.removeAdminRole("Bearer $token", chatId, userId)
            if (response.isSuccessful) {
                return AppResponse.success(Unit)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun getChats(): AppResponse<List<Chat>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.getChatsForUser("Bearer $token")
            if (response.isSuccessful) {
                val chats = response.body()?.data?.map { it.toChat() } ?: emptyList()
                return AppResponse.success(chats)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun getChatById(chatId: Int): AppResponse<Chat> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.getChatById(token = "Bearer $token", id = chatId)
            if (response.isSuccessful) {
                val chat =
                    response.body()?.data?.toChat() ?: return AppResponse.failed(
                        ServerErrorException
                    )
                return AppResponse.success(chat)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun getDialogChatByUsers(dialogUserId: Int): AppResponse<Chat> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.getDialogChatByUsers(
                token = "Bearer $token",
                dialogUserId = dialogUserId,
            )
            if (response.isSuccessful) {
                val chat = response.body()?.data?.toChat() ?: return AppResponse.failed(
                    ServerErrorException
                )
                return AppResponse.success(chat)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }

    override suspend fun getMessagesForChat(
        chatId: Int,
        page: Int,
        pageSize: Int,
    ): AppResponse<List<Message>> {
        return try {
            if (!NetworkUtil.isNetworkAvailable(application)) {
                return AppResponse.failed(PoorNetworkConnectionException)
            }

            val token = authManager.token ?: return AppResponse.failed(UnauthorizedException)
            val response = chatApiService.getMessagesForChat(
                token = "Bearer $token",
                chatId = chatId,
                page = page,
                pageSize = pageSize
            )
            if (response.isSuccessful) {
                val messages = response.body()?.data?.map { it.toMessage() } ?: emptyList()
                return AppResponse.success(messages)
            }

            val failedApiResponse = gson.fromJson(
                response.errorBody()?.charStream(), FailedApiResponse::class.java
            )
            val exception = failedApiResponse.toException()

            return AppResponse.failed(
                exception = exception,
                message = failedApiResponse?.message
            )
        } catch (e: IOException) {
            AppResponse.failed(e, application.getString(R.string.error_couldnt_reach_server))
        } catch (e: Exception) {
            e.printStackTrace()
            AppResponse.failed(e, application.getString(R.string.error_unknown))
        }
    }


    private fun handleWebSocketEvent(
        gson: Gson,
        frameText: String?,
    ): ChatEvent {

        if (frameText == null) {
            throw Exception("Unexpected behaviour")
        }

        val delimiterIndex = frameText.indexOf("#")
        if (delimiterIndex == -1) {
            println("No delimiter found")
            throw Exception()
        }
        val type = frameText.substring(0, delimiterIndex)
        val json = frameText.substring(delimiterIndex + 1, frameText.length)
        return when (WebSocketEvent.valueOf(type)) {
            WebSocketEvent.SEND_MESSAGE -> {
                val messageResponse = gson.fromJson(json, MessageResponse::class.java)
                ChatEvent.MessageSent(message = messageResponse.toMessage())
            }

            WebSocketEvent.EDIT_MESSAGE,
            WebSocketEvent.READ_MESSAGE,
            -> {
                val messageResponse = gson.fromJson(json, MessageResponse::class.java)
                ChatEvent.MessageUpdated(message = messageResponse.toMessage())
            }

            WebSocketEvent.DELETE_MESSAGE -> {
                val messageResponse = gson.fromJson(json, MessageResponse::class.java)
                ChatEvent.MessageDeleted(message = messageResponse.toMessage())
            }

            WebSocketEvent.CREATE_CHAT -> {
                val chatResponse = gson.fromJson(json, ChatResponse::class.java)
                ChatEvent.ChatCreated(chatResponse.toChat())
            }

            WebSocketEvent.LEAVE_CHAT -> {
                val userId = json.toInt()
                ChatEvent.LeftChat(userId)
            }

            WebSocketEvent.DELETE_CHAT -> {
                val chatResponse = gson.fromJson(json, ChatResponse::class.java)
                ChatEvent.ChatDeleted(chatResponse.toChat())
            }

            WebSocketEvent.UPDATE_CHAT -> {
                val chatResponse = gson.fromJson(json, ChatResponse::class.java)
                ChatEvent.ChatUpdated(chatResponse.toChat())
            }

            WebSocketEvent.DELETE_CHAT_PARTICIPANT -> {
                val userId = json.toInt()
                ChatEvent.DeleteChatParticipant(userId)
            }

            WebSocketEvent.ADD_CHAT_PARTICIPANTS -> {
                val participants = gson.fromJson(json, Array<ChatParticipantResponse>::class.java)
                    .map { it.toChatParticipant() }
                ChatEvent.AddChatParticipants(participants)
            }
        }
    }

}