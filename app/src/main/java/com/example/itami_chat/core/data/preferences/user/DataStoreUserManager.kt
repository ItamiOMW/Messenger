package com.example.itami_chat.core.data.preferences.user

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.itami_chat.core.data.mapper.toDataStoreUser
import com.example.itami_chat.core.data.mapper.toMyUser
import com.example.itami_chat.core.domain.model.MyUser
import com.example.itami_chat.core.domain.preferences.UserManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreUserManager @Inject constructor(
    @ApplicationContext context: Context,
) : UserManager {

    private val dataStore = DataStoreFactory.create(DataStoreUserSerializer, produceFile = {
        context.dataStoreFile(USER_DATASTORE_NAME)
    })

    override suspend fun setUser(user: MyUser?) {
        dataStore.updateData {
            user?.toDataStoreUser()
                ?: DataStoreUser()
        }
    }

    override val user: Flow<MyUser>
        get() = dataStore.data.map { it.toMyUser() }


    companion object {

        private const val USER_DATASTORE_NAME = "user_datastore"

    }
}
