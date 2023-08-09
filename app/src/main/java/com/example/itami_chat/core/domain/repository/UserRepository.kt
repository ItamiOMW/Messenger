package com.example.itami_chat.core.domain.repository

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.UpdateProfileData
import com.example.itami_chat.core.domain.model.UserProfile

interface UserRepository {

    suspend fun updateProfile(
        updateProfileData: UpdateProfileData,
        profilePictureUri: String?,
    ): AppResponse<Unit>

    suspend fun getUserProfile(id: Int): AppResponse<UserProfile>

    suspend fun blockUser(id: Int): AppResponse<Unit>

    suspend fun unblockUser(id: Int): AppResponse<Unit>

    suspend fun logout(): AppResponse<Unit>

}