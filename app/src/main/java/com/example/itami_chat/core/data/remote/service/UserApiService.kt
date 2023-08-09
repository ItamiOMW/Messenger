package com.example.itami_chat.core.data.remote.service

import com.example.itami_chat.authentication_feature.data.remote.dto.response.MyUserResponse
import com.example.itami_chat.core.data.remote.dto.response.ApiResponse
import com.example.itami_chat.core.data.remote.dto.response.ProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApiService {


    @Multipart
    @PUT("api/v1/users/profile/update")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part updateProfileData: MultipartBody.Part,
        @Part profilePicture: MultipartBody.Part?,
    ): Response<ApiResponse<MyUserResponse>>


    @GET("api/v1/users/profile/{id}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<ApiResponse<ProfileResponse>>


    @POST("api/v1/users/block/{id}")
    suspend fun blockUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<ApiResponse<Unit>>


    @POST("api/v1/users/unblock/{id}")
    suspend fun unblockUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<ApiResponse<Unit>>

}