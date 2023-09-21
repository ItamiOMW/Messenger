package com.example.itami_chat.settings_feature.data.remote

import com.example.itami_chat.core.data.remote.dto.response.ApiResponse
import com.example.itami_chat.core.data.remote.dto.response.SimpleUserResponse
import com.example.itami_chat.settings_feature.data.remote.dto.ChangeMessagesPermission
import com.example.itami_chat.settings_feature.data.remote.dto.ChangePasswordRequest
import com.example.itami_chat.settings_feature.data.remote.dto.VerifyPasswordChangeCodeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserSettingsApiService {

    @POST("api/v1/auth/send-password-change-code")
    suspend fun sendPasswordChangeCode(
        @Header("Authorization") token: String,
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/auth/verify-password-change")
    suspend fun verifyPasswordChangeCode(
        @Header("Authorization") token: String,
        @Body verifyPasswordChangeCodeRequest: VerifyPasswordChangeCodeRequest,
    ): Response<ApiResponse<Unit>>

    @POST("api/v1/auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ApiResponse<Unit>>

    @PUT("api/v1/auth/account/permission/messages")
    suspend fun changeMessagesPermission(
        @Header("Authorization") token: String,
        @Body changeMessagesPermission: ChangeMessagesPermission,
    ): Response<ApiResponse<Unit>>

    @DELETE("api/v1/auth/account")
    suspend fun deleteAccount(
        @Header("Authorization") token: String,
    ): Response<ApiResponse<Unit>>

    @GET("api/v1/users/blocked")
    suspend fun getBlockedUsers(
        @Header("Authorization") token: String,
    ): Response<ApiResponse<List<SimpleUserResponse>>>

}