package com.example.itami_chat.core.domain.preferences

import com.example.itami_chat.core.domain.model.MyUser
import kotlinx.coroutines.flow.Flow

interface UserManager {

    suspend fun setUser(user: MyUser?)

    val user: Flow<MyUser>

}