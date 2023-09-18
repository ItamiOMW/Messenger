package com.example.itami_chat.core.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.repository.UserRepository

class BlockUserUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(userId: Int): AppResponse<Unit> {
        return userRepository.blockUser(userId)
    }

}