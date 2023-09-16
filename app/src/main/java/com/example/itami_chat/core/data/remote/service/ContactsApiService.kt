package com.example.itami_chat.core.data.remote.service

import com.example.itami_chat.core.data.remote.dto.response.ApiResponse
import com.example.itami_chat.core.data.remote.dto.response.SimpleUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ContactsApiService {


    @GET("api/v1/contacts")
    suspend fun getContacts(
        @Header("Authorization") token: String,
    ): Response<ApiResponse<List<SimpleUserResponse>>>


}