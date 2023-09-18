package com.example.itami_chat.core.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.repository.UserRepository

class GetUsersByIdsUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(userIds: List<Int>): AppResponse<List<SimpleUser>> {
        return userRepository.getUsersByIds(userIds)
    }

}