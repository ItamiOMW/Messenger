package com.example.itami_chat.chat_feature.data.remote

import com.example.itami_chat.chat_feature.data.remote.dto.ChatParticipantResponse
import com.example.itami_chat.chat_feature.data.remote.dto.ChatResponse
import com.example.itami_chat.chat_feature.data.remote.dto.MessageResponse
import com.example.itami_chat.core.data.remote.dto.response.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {

    @Multipart
    @POST("api/v1/chats")
    suspend fun createChat(
        @Header("Authorization") token: String,
        @Part createChatRequest: MultipartBody.Part,
        @Part chatPicture: MultipartBody.Part?,
    ): Response<ApiResponse<ChatResponse>>

    @Multipart
    @PUT("api/v1/chats/{id}")
    suspend fun updateChat(
        @Header("Authorization") token: String,
        @Path("id") chatId: Int,
        @Part updateChatRequest: MultipartBody.Part,
        @Part chatPicture: MultipartBody.Part?,
    ): Response<ApiResponse<ChatResponse>>

    @DELETE("api/v1/chats/{id}")
    suspend fun deleteChat(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<ApiResponse<Unit>>

    @DELETE("api/v1/chats/{id}/participants/{userId}")
    suspend fun deleteChatParticipant(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Path("userId") userId: Int,
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/chats/{id}/participants")
    suspend fun addChatParticipants(
        @Header("Authorization") token: String,
        @Path("id") chatId: Int,
        @Query("ids") userIds: List<Int>,
    ): Response<ApiResponse<List<ChatParticipantResponse>>>

    @PUT("api/v1/chats/{id}/participants/{userId}/admin")
    suspend fun assignAdminRole(
        @Header("Authorization") token: String,
        @Path("id") chatId: Int,
        @Path("userId") userId: Int,
    ): Response<ApiResponse<Unit>>

    @DELETE("api/v1/chats/{id}/participants/{userId}/admin")
    suspend fun removeAdminRole(
        @Header("Authorization") token: String,
        @Path("id") chatId: Int,
        @Path("userId") userId: Int,
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/chats/{id}/leave")
    suspend fun leaveChat(
        @Header("Authorization") token: String,
        @Path("id") chatId: Int,
    ): Response<ApiResponse<Unit>>

    @GET("api/v1/chats")
    suspend fun getChatsForUser(
        @Header("Authorization") token: String,
    ): Response<ApiResponse<List<ChatResponse>>>

    @GET("api/v1/chats/{id}")
    suspend fun getChatById(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<ApiResponse<ChatResponse>>

    @GET("api/v1/chats/dialog/{userId}")
    suspend fun getDialogChatByUsers(
        @Header("Authorization") token: String,
        @Path("userId") dialogUserId: Int,
    ): Response<ApiResponse<ChatResponse>>

    @GET("api/v1/chats/{id}/messages/{page}/{pageSize}")
    suspend fun getMessagesForChat(
        @Header("Authorization") token: String,
        @Path("id") chatId: Int,
        @Path("page") page: Int,
        @Path("pageSize") pageSize: Int,
    ): Response<ApiResponse<List<MessageResponse>>>

}