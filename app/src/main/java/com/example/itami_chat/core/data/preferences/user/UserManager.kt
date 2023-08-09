package com.example.itami_chat.core.data.preferences.user

import kotlinx.coroutines.flow.Flow

interface UserManager {

    suspend fun setUser(user: DataStoreUser?)

    val user: Flow<DataStoreUser>

}