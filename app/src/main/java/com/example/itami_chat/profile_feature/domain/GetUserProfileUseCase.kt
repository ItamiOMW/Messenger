package com.example.itami_chat.profile_feature.domain

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.UserProfile
import com.example.itami_chat.core.domain.repository.UserRepository

class GetUserProfileUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(userId: Int): AppResponse<UserProfile> {
        return userRepository.getUserProfile(userId)
    }

}