package com.example.itami_chat.core.domain.use_case

import com.example.itami_chat.core.domain.model.AppResponse
import com.example.itami_chat.core.domain.model.SimpleUser
import com.example.itami_chat.core.domain.repository.UserRepository

class SearchForUsersUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(query: String): AppResponse<List<SimpleUser>> {
        return userRepository.searchForUsers(query)
    }

}