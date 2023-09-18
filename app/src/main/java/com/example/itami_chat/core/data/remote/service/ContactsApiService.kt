package com.example.itami_chat.core.data.remote.service

import com.example.itami_chat.core.data.remote.dto.response.ApiResponse
import com.example.itami_chat.core.data.remote.dto.response.ContactRequestResponse
import com.example.itami_chat.core.data.remote.dto.response.SimpleUserResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ContactsApiService {


    @GET("api/v1/contacts")
    suspend fun getContacts(
        @Header("Authorization") token: String,
    ): Response<ApiResponse<List<SimpleUserResponse>>>

    @DELETE("api/v1/contacts/{userId}")
    suspend fun deleteContact(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
    ): Response<ApiResponse<List<SimpleUserResponse>>>

    @POST("api/v1/contacts/requests/send/{id}")
    suspend fun sendContactRequest(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
    ): Response<ApiResponse<ContactRequestResponse>>

    @POST("api/v1/contacts/requests/{id}/accept")
    suspend fun acceptContactRequest(
        @Header("Authorization") token: String,
        @Path("id") requestId: Int,
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/contacts/requests/{id}/decline")
    suspend fun declineContactRequest(
        @Header("Authorization") token: String,
        @Path("id") requestId: Int,
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/contacts/requests/{id}/cancel")
    suspend fun cancelContactRequest(
        @Header("Authorization") token: String,
        @Path("id") requestId: Int,
    ): Response<ApiResponse<Unit>>

}